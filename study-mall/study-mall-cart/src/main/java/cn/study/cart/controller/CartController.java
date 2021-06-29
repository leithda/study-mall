package cn.study.cart.controller;


import cn.study.cart.service.CartService;
import cn.study.cart.vo.Cart;
import cn.study.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class CartController {

    @Autowired
    CartService cartService;


    
    @GetMapping("currentUserCartItems")
    @ResponseBody
    public List<CartItem> currentUserCartItems(){
        return cartService.getUserCartItems();
    }

    @GetMapping("checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("check") Integer check) {
        cartService.check(skuId,check);
        return "redirect:http://cart.mall.com/cart.html";
    }

    /**
     * 在cookie中保存用户标识，用于区分未登录用户
     * <p>
     * 登录后，session有
     * 没登录，按照cookie中生成的用户令牌来做
     * 第一次，如果没有令牌，创建一个用户令牌
     */
    @GetMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cartList";
    }

    /**
     * 添加商品到购物车
     */
    @GetMapping("addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes ra) throws ExecutionException, InterruptedException {

        /*CartItem cartItem = */
        cartService.addToCart(skuId, num);
//        model.addAttribute("item",cartItem);
        ra.addAttribute("skuId", skuId);
        return "redirect:http://cart.mall.com/addToCartSuccess.html";
    }


    /**
     * 跳转到购物车添加成功页面
     *
     * @param skuId 商品ID
     */
    @GetMapping("addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,
                                       Model model) {
        // 重定向到成功页面，再次查询购物车数据
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item", cartItem);
        return "success";
    }


}
