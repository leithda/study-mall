package cn.study.product.service;

import cn.study.common.utils.PageUtils;
import cn.study.product.entity.SkuInfoEntity;
import cn.study.product.entity.vo.SkuItemVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 22:32:21
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存Sku信息
     * @param skuInfoEntity sku信息
     */
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    /**
     * 根据条件获取 sku 商品列表
     * @param params 参数
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 根据SpuId获取所有的sku信息
     * @param spuId SpuId
     */
    List<SkuInfoEntity> getSkusBySpuId(Long spuId);

    /**
     * 根据 skuId 查询商品详情
     * @param skuId skuId
     */
    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;


}

