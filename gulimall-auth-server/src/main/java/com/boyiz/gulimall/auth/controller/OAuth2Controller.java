package com.boyiz.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.boyiz.common.exception.BizCodeEnume;
import com.boyiz.common.utils.HttpUtils;
import com.boyiz.common.utils.R;
import com.boyiz.gulimall.auth.feign.MemberFeignService;
import com.boyiz.gulimall.auth.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理社交登录
 */
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    MemberFeignService memberFeignService;

    //TODO: 开发者账户信息 抽取配置文件
    @GetMapping("/weibo/success")
    public R weibo(@RequestParam("code") String code) throws Exception {
        //https://api.weibo.com/oauth2/authorize?client_id=YOUR_CLIENT_ID&response_type=code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI
        //https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "");
        map.put("client_secret", "");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://gulimall.com/oauth2/weibo/success");
        map.put("code", code);
        //根据code换取access_token
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", new HashMap<>(), map, new HashMap<>());
        if (response.getStatusLine().getStatusCode() == 200) {
            //获取到access_token
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            //判断用户是否已注册
            //登录或注册，调用member远程方法
            R r = memberFeignService.authLogin(socialUser);
            return R.ok().put("data", r.get("data"));
        } else {
            return R.error(BizCodeEnume.USER_AUTHLOGIN_EXCEPTION.getCode(),BizCodeEnume.USER_AUTHLOGIN_EXCEPTION.getMessage());
        }
    }
}
