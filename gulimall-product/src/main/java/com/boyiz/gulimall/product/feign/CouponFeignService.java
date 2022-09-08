package com.boyiz.gulimall.product.feign;

import com.boyiz.common.to.SkuReductionTo;
import com.boyiz.common.to.SpuBoundTo;
import com.boyiz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-coupon") //调用gulimall-coupon 服务
public interface CouponFeignService {

    /**
     * 此方法调用gulimall-coupon远程服务
     * 调用服务中的 @PostMapping("coupon/spubounds/save") 这个请求
     * @param spuBoundTo
     * @return
     */
    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);

    /**
     * 流程：
     *  在SpuInfoServiceImpl 实现中，调用 couponFeignService.saveSpuBounds(spuBoundTo);
     *  1、@RequestBody将spuBoundTo 转为Json
     *  2、找到gulimall-coupon远程服务，给 coupon/spubounds/save 发送请求
     *      同时将Json放在请求体中
     *  3、对方服务接收到请求，@RequestBody 将Json转换为实体
     *  ⚠️只要Json数据模型互相兼容，双方服务无需使用同一个TO
     */
}
