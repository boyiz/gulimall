package com.boyiz.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.member.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:31:41
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

