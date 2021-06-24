package cn.study.cart.service;


import cn.study.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

public interface CartService {
    /**
     * 添加商品到购物车
     * @param skuId 商品ID
     * @param num 数量
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;
}
