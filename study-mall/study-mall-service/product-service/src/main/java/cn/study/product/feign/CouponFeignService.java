package cn.study.product.feign;

import cn.study.common.constant.ServiceNameConstant;
import cn.study.common.to.SkuReductionTo;
import cn.study.common.to.SpuBoundsTo;
import cn.study.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 优惠卷远程调用
 */
@FeignClient(ServiceNameConstant.COUPON_SERVICE)
public interface CouponFeignService {

    /**
     * 保存商品积分信息
     * @param spuBoundsTo 商品积分信息
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);


    /**
     * 保存商品满减信息
     * @param skuReductionTo 商品满减信息
     */
    @PostMapping("coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
