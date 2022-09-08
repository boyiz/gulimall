package com.boyiz.gulimall.cart.controller;

import com.boyiz.common.utils.R;
import com.boyiz.gulimall.cart.exception.CartExceptionHandler;
import com.boyiz.gulimall.cart.interceptor.CartInterceptor;
import com.boyiz.gulimall.cart.service.CartService;
import com.boyiz.gulimall.cart.to.UserInfoTo;
import com.boyiz.gulimall.cart.vo.CartItemVo;
import com.boyiz.gulimall.cart.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    CartService cartService;

//    @GetMapping("/currentUserCartItems")
//    public R currentUserCartItems() throws CartExceptionHandler {
//        List<CartItemVo> list = cartService.getUserCartItems();
//        return R.ok().put("data", list);
//    }

    @GetMapping("/cartList")
    public R cartListPage() throws ExecutionException, InterruptedException {
//        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
//        System.out.println("---------------"+userInfoTo);
        CartVo cartVo = cartService.getCart();
        return R.ok().put("data", cartVo);
    }

    /**
     * 添加商品到购物车
     * attributes.addFlashAttribute():将数据放在session中，可以在页面中取出，但是只能取一次
     * attributes.addAttribute():将数据放在url后面
     *
     * @return
     */
    @GetMapping(value = "/addCartItem")
    public R addCartItem(@RequestParam("skuId") Long skuId,
                         @RequestParam("num") Integer num)
            throws ExecutionException, InterruptedException {

        CartItemVo cartItemVo = cartService.addToCart(skuId, num);
        return R.ok().put("data", cartItemVo);
    }

    /**
     * 商品是否选中
     *
     * @param skuId
     * @param checked
     * @return
     */
    @GetMapping(value = "/checkItem")
    public R checkItem(@RequestParam(value = "skuId") Long skuId,
                       @RequestParam(value = "checked") Integer checked) {

        cartService.checkItem(skuId, checked);
        return R.ok();

    }

    /**
     * 改变商品数量
     *
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping(value = "/countItem")
    public R countItem(@RequestParam(value = "skuId") Long skuId,
                       @RequestParam(value = "num") Integer num) {
        cartService.changeItemCount(skuId, num);
        return R.ok();
    }

    /**
     * 删除商品信息
     *
     * @param skuId
     * @return
     */
    @GetMapping(value = "/deleteItem")
    public R deleteItem(@RequestParam("skuId") Integer skuId) {
        cartService.deleteIdCartInfo(skuId);
        return R.ok();

    }

    /**
     * 获取当前用户的购物车商品项
     *
     * @return
     */
    @GetMapping(value = "/currentUserCartItems/{userId}")
    @ResponseBody
    public R getCurrentCartItems(@PathVariable("userId")Long userId) throws CartExceptionHandler {
        List<CartItemVo> list =  cartService.getUserCartItems(userId);
        return R.ok().setData(list);

    }

}
