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
     * 1、CouponFeignService.saveSpuBounds(spuBoundTo);
     *      1）、@RequestBody将这个对象转为json。
     *      2）、找到 coupon-service 服务，给/coupon/spubounds/save发送请求。
     *          将上一步转的json放在请求体位置，发送请求；
     *      3）、对方服务收到请求。请求体里有json数据。
     *          (@RequestBody SpuBoundsEntity spuBounds)；将请求体的json转为SpuBoundsEntity；
     * 只要json数据模型是兼容的。双方服务无需使用同一个to
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
