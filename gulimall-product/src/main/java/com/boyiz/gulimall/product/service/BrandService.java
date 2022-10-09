package com.boyiz.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.product.entity.BrandEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;

import java.util.Map;

/**
 * 品牌
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 16:14:05
 */

public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetial(BrandEntity brandEntity);
}

