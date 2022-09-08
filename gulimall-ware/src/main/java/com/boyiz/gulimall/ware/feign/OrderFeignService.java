package com.boyiz.gulimall.ware.feign;

import com.boyiz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName OrderFeignService
 * @Description TODO
 * @Author boyiz
 * @Date 2022/8/20 16:49
 * @Version 1.0
 **/
@FeignClient("gulimall-order")
public interface OrderFeignService {

    @GetMapping("/status/{orderSn}")
    R getOrderStatus(@PathVariable("orderSn") String orderSn);
}
