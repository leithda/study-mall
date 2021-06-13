package cn.study.search.service.es;

import cn.study.search.entity.EsProductEntity;

import java.util.List;

public interface EsProductSaveService {
    /**
     * 保存商品信息
     * @param entityList 商品信息
     */
    void productStatusUp(List<EsProductEntity> entityList);
}
