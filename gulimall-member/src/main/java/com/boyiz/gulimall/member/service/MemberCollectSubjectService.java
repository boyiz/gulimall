package com.boyiz.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.member.entity.MemberCollectSubjectEntity;

import java.util.Map;

/**
 * 会员收藏的专题活动
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:31:41
 */
public interface MemberCollectSubjectService extends IService<MemberCollectSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

