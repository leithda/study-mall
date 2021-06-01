package cn.study.search.service;

import cn.study.search.entity.ProductEntity;

import java.util.List;

public interface ProductSaveService {
    /**
     * 保存商品信息
     * @param entityList 商品信息
     */
    void productStatusUp(List<ProductEntity> entityList);
}
