package com.boyiz.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:23:57
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

