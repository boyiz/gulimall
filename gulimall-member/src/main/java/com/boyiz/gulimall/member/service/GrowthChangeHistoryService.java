package com.boyiz.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.member.entity.GrowthChangeHistoryEntity;

import java.util.Map;

/**
 * 成长值变化历史记录
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:31:41
 */
public interface GrowthChangeHistoryService extends IService<GrowthChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

