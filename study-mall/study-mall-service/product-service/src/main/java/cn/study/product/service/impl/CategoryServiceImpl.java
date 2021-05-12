package cn.study.product.service.impl;

import cn.study.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
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