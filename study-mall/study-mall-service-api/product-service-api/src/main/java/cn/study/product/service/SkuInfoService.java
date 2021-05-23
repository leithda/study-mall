package cn.study.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.product.entity.SkuInfoEntity;

import java.util.Map;

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
}

