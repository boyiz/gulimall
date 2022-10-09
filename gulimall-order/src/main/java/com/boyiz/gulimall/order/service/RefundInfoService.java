package com.boyiz.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:41:13
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

