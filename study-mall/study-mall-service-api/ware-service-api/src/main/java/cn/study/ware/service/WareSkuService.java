package cn.study.ware.service;

import cn.study.common.to.SkuHasStockTo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:10:48
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加库存
     * @param skuId sku id
     * @param wareId ware id
     * @param skuNum 数量
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
     * 获取sku是否有库存
     * @param skuIds sku Id 集合
     */
    List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds);
}

