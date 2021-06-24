package cn.study.cart.controller;


import cn.study.cart.interceptor.CartInterceptor;
import cn.study.cart.service.CartService;
import cn.study.cart.vo.CartItem;
import cn.study.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;

@Controller
public class CartController {

    @Autowired
    CartService cartService;

    /**
     * 在cookie中保存用户标识，用于区分未登录用户
     *
     * 登录后，session有
     * 没登录，按照cookie中生成的用户令牌来做
     * 第一次，如果没有令牌，创建一个用户令牌
     * @param session session
     */
    @GetMapping("/cart.html")
    public String cartListPage(HttpSession session){

        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        return "cartList";
    }

    /**
     * 添加商品到购物车
     */
    @GetMapping("addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            Model model) throws ExecutionException, InterruptedException {

        CartItem cartItem = cartService.addToCart(skuId,num);
        model.addAttribute("item",cartItem);
        return "success";
    }


}
