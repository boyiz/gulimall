package com.boyiz.gulimall.order.feign;

import com.boyiz.common.utils.R;
import com.boyiz.gulimall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("gulimall-cart")
public interface CartFeignService {

    @GetMapping("/cart/currentUserCartItems/{userId}")
    R getCurrentCartItems(@PathVariable("userId") Long userId);
}
