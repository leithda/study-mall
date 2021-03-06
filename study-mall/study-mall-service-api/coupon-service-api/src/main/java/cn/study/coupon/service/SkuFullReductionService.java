package cn.study.coupon.service;

import cn.study.common.to.SkuReductionTo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:01:46
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存商家商品的满减信息
     * @param skuReductionTo 商品满减信息
     */
    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

