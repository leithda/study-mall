package cn.study.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.Query;

import cn.study.product.dao.CategoryDao;
import cn.study.product.entity.CategoryEntity;
import cn.study.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取分类树
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