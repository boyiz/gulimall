package com.boyiz.gulimall.order.feign;

import com.boyiz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("/product/spuinfo/skuId/{id}")
    public R getSpuInfoBySkuId(@PathVariable("id") Long id);
}
