package cn.study.cart.service.impl;

import cn.study.cart.feign.ProductFeignService;
import cn.study.cart.interceptor.CartInterceptor;
import cn.study.cart.service.CartService;
import cn.study.cart.vo.CartItem;
import cn.study.cart.vo.SkuInfoVo;
import cn.study.cart.vo.UserInfoTo;
import cn.study.common.utils.R;
import cn.study.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    private final String CART_PREFIX = "mall:cart:";

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        String res = (String) cartOps.get(skuId.toString());

        if(StringUtils.isEmpty(res)){
            // 购物车没有此商品
            CartItem cartItem = new CartItem();
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                // 远程查询出商品信息
                R skuInfo = productFeignService.getSkuInfo(skuId);
                SkuInfoVo skuInfoVo = JSON.parseObject(JSON.toJSONString(skuInfo.get("skuInfo")), SkuInfoVo.class);
                // 商品添加到购物车
                cartItem.setCheck(true);
                cartItem.setCount(1);
                cartItem.setImage(skuInfoVo.getSkuDefaultImg());
                cartItem.setTitle(skuInfoVo.getSkuTitle());
                cartItem.setPrice(skuInfoVo.getPrice());
                cartItem.setSkuId(skuInfoVo.getSkuId());
            }, executor);


            CompletableFuture<Void> getSkuSaleAttrValuesTask = CompletableFuture.runAsync(() -> {
                List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(skuSaleAttrValues);
            }, executor);

            CompletableFuture.allOf(getSkuInfoTask,getSkuSaleAttrValuesTask).get();
            // 远程查询sku组合信息查询
            String cartItemJson = JSON.toJSONString(cartItem);
            cartOps.put(cartItem.getSkuId().toString(),cartItemJson);
            return cartItem;
        }else{
            // 购物车有此商品，修改数量
            CartItem cartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(cartItem.getCount()+num);
            cartOps.put(skuId.toString(),JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String itemJson = (String) cartOps.get(skuId.toString());
        return JSON.parseObject(itemJson, CartItem.class);
    }

    /**
     * 获取到要操作的购物车
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        // 1、判断
        String cartKey = "";
        if (Objects.nonNull(userInfoTo.getUserId())) {
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }

        // 购物车
        return redisTemplate.boundHashOps(cartKey);
    }
}
