package cn.study.product.service.impl;

import cn.study.common.utils.StringUtils;
import cn.study.product.entity.vo.Catelog2Vo;
import cn.study.product.service.CategoryBrandRelationService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.Query;

import cn.study.product.dao.CategoryDao;
import cn.study.product.entity.CategoryEntity;
import cn.study.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取分类树
     *
     * @param params 参数
     */
    @Override
    public List<CategoryEntity> tree(Map<String, Object> params) {
        List<CategoryEntity> categoryList = list();
        List<CategoryEntity> rootCategoryList = categoryList.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());

        rootCategoryList.forEach(categoryEntity -> setCategoryChildren(categoryEntity, categoryList));
        return rootCategoryList;
    }

    /**
     * 批量删除分类
     *
     * @param idList 分类ID集合
     */
    @Override
    public void removeCategoryByIds(List<Long> idList) {
        // TODO: 检查分类是否被使用

        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);


        return parentPath.toArray(new Long[0]);
    }

    /**
     * 级联更新所有的关联数据
     *
     * @param category 分类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCascade(CategoryEntity category) {
        updateById(category);

        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        String catelogJson = redisTemplate.opsForValue().get("catelogJson");
        if(StringUtils.isEmpty(catelogJson)){
            Map<String, List<Catelog2Vo>> catelogJsonFromDb = getCatelogJsonFromDb();
            String s = JSON.toJSONString(catelogJsonFromDb);
            redisTemplate.opsForValue().set("catelogJson",s);
            return catelogJsonFromDb;
        }
        return JSON.parseObject(catelogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
    }


    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDb() {

        List<CategoryEntity> allCategoryEntityList = baseMapper.selectList(null);

        // 查出所有1级分类
        List<CategoryEntity> level1Categorys = getCatergoryListByPid(allCategoryEntityList,0L);

        // 封装数据
        return level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 每一个1级节点，查询2级分类
            List<CategoryEntity> level2Categorys = getCatergoryListByPid(allCategoryEntityList,v.getCatId());

            // 封装2级分类
            List<Catelog2Vo> catelog2Vos = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(level2Categorys)) {
                catelog2Vos = level2Categorys.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), l2.getName(), l2.getCatId().toString(), null);
                    // 查询3级分类
                    List<CategoryEntity> level3Catelogs = getCatergoryListByPid(allCategoryEntityList,l2.getCatId());
                    if(CollectionUtils.isNotEmpty(level3Catelogs)){
                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = level3Catelogs
                                .stream()
                                .map(l3 -> new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(),l3.getCatId().toString(),l3.getName()))
                                .collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catelog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
    }

    /**
     * 根据父ID获取子分类列表
     * @param allCategoryEntityList 全量分类数据
     * @param parentCid 父分类ID
     */
    private List<CategoryEntity> getCatergoryListByPid(List<CategoryEntity> allCategoryEntityList, Long parentCid) {
        return allCategoryEntityList.stream().filter(item->item.getParentCid().equals(parentCid)).collect(Collectors.toList());
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    /**
     * 设置分类的子节点
     *
     * @param parentCategory 父级分类
     * @param categoryList   所有节点
     */
    private void setCategoryChildren(CategoryEntity parentCategory, List<CategoryEntity> categoryList) {
        List<CategoryEntity> childrenCategoryList = categoryList.stream().filter(c -> c.getParentCid().equals(parentCategory.getCatId())).collect(Collectors.toList());
        parentCategory.setChildren(childrenCategoryList);
        childrenCategoryList.forEach(childCategory -> setCategoryChildren(childCategory, categoryList));
    }

}