package com.boyiz.gulimall.product.fallback;

import com.boyiz.common.exception.BizCodeEnume;
import com.boyiz.common.utils.R;
import com.boyiz.gulimall.product.feign.SeckillFeignService;
import org.springframework.stereotype.Component;


@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSkuSeckilInfo(Long skuId) {
        return R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(),BizCodeEnume.TO_MANY_REQUEST.getMessage());
    }
}
