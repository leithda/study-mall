package cn.study.cart.service;


import cn.study.cart.vo.Cart;
import cn.study.cart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CartService {
    /**
     * 添加商品到购物车
     * @param skuId 商品ID
     * @param num 数量
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车中指定购物想
     * @param skuId 商品ID
     */
    CartItem getCartItem(Long skuId);

    /**
     * 获取购物车信息
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车
     * @param cartKey 购物车的key
     */
    void clearCart(String cartKey);

    /**
     * 勾选购物项
     * @param skuId 商品ID
     * @param check 是否勾选
     */
    void check(Long skuId, Integer check);

    /**
     * 获取登录用户的购物项
     */
    List<CartItem> getUserCartItems();
}
