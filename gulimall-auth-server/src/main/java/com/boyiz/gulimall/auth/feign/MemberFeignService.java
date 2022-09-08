package com.boyiz.gulimall.auth.feign;

import com.boyiz.common.utils.R;
import com.boyiz.gulimall.auth.vo.SocialUser;
import com.boyiz.gulimall.auth.vo.UserLoginVo;
import com.boyiz.gulimall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    public R regist(@RequestBody UserRegisterVo userRegisterVo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoginVo loginVo);

    @PostMapping("/member/member/oauth2/login")
    public R authLogin(@RequestBody SocialUser socialUser) throws Exception;

}

