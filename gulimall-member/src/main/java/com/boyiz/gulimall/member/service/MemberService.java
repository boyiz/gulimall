package com.boyiz.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.member.entity.MemberEntity;
import com.boyiz.gulimall.member.exception.PhoneException;
import com.boyiz.gulimall.member.exception.UsernameException;
import com.boyiz.gulimall.member.vo.MemberUserLoginVo;
import com.boyiz.gulimall.member.vo.MemberUserRegisterVo;
import com.boyiz.gulimall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:31:41
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberUserRegisterVo memberUserRegisterVo);

    void checkPhoneUnique(String Phone) throws PhoneException;

    void checkUsernameUnique(String UserName) throws UsernameException;

    MemberEntity login(MemberUserLoginVo loginVo);

    MemberEntity login(SocialUser socialUser) throws Exception ;
}

