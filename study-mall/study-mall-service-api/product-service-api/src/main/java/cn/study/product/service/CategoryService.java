package cn.study.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分类树
     * @param params 参数
     */
    List<CategoryEntity> tree(Map<String, Object> params);

    /**
     * 批量删除分类
     * @param idList 分类ID集合
     */
    void removeCategoryByIds(List<Long> idList);

    /**
     * 获取分类路径
     * @param catelogId 分类ID
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * 级联更新所有的关联数据
     * @param category 分类
     */
    void updateCascade(CategoryEntity category);
}

