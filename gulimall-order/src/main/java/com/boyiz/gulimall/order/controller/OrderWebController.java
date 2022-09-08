package com.boyiz.gulimall.order.controller;

import com.boyiz.common.exception.BizCodeEnume;
import com.boyiz.common.utils.R;
import com.boyiz.gulimall.order.service.OrderService;
import com.boyiz.gulimall.order.vo.OrderConfirmVo;
import com.boyiz.gulimall.order.vo.OrderSubmitVo;
import com.boyiz.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@RestController
public class OrderWebController {

    @Autowired
    OrderService orderService;



    @GetMapping("/toTrade")
    public R toTrade(HttpServletRequest request) throws ExecutionException, InterruptedException {

        OrderConfirmVo confirmVo = orderService.confirmOrder();

        return R.ok().put("data",confirmVo);
    }

    @PostMapping("/submitOrder")
    public R submitOrder(OrderSubmitVo orderSubmitVo) {

        SubmitOrderResponseVo submitOrderResponseVo = orderService.submitOrder(orderSubmitVo);
        if (submitOrderResponseVo.getCode() == 0) {
            //SUCCESS
            return R.ok().put("data", submitOrderResponseVo);
        }
        return R.error();
    }
}
