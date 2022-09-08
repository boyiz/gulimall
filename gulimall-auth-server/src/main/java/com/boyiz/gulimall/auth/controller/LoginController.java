package com.boyiz.gulimall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.boyiz.common.constant.AuthServerConstant;
import com.boyiz.common.exception.BizCodeEnume;
import com.boyiz.common.utils.R;
import com.boyiz.gulimall.auth.feign.MemberFeignService;
import com.boyiz.gulimall.auth.feign.ThridPartFeignService;
import com.boyiz.gulimall.auth.vo.UserLoginVo;
import com.boyiz.gulimall.auth.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sms")
public class LoginController {

    @Autowired
    ThridPartFeignService thridPartFeignService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberFeignService memberFeignService;

    @GetMapping("/sendcode")
    public R sendCode(@RequestParam("phone") String phone) {
        //TODO 1、接口防刷
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)) {
            Long time = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - time < 60000) {
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(), BizCodeEnume.SMS_CODE_EXCEPTION.getMessage());
            }
        }

        //2、验证码的再次校验，redis： key-》phone，value-》code
        //sms:code:17777777777 -> 123456
        //加上系统时间判断防止被刷验证码
        String code = UUID.randomUUID().toString().substring(0, 6);
        //redis缓存验证码，防止同一个phone在60s内再次发送
        redisTemplate.opsForValue().setIfAbsent(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, code + "_" + System.currentTimeMillis(), 10, TimeUnit.MINUTES);
        thridPartFeignService.sendCode(phone, code);
        return R.ok();
    }

    @PostMapping("/regist")
    public R regist(@RequestBody @Valid UserRegisterVo userRegisterVo, BindingResult result) {
        //1、数据校验
        if (result.hasErrors()) {
            Map<String, String> errorMap = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (item1, item2) -> item1));
            return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMessage()).put("data", errorMap);
        }

        //2、注册，调用远程服务
        // 2.1 校验验证码
        String code = userRegisterVo.getCode();
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getPhone());
        if (!StringUtils.isEmpty(redisCode)) {
            if (code.equals(redisCode.split("_")[0])) {
                //删除验证码，令牌机制
//                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getPhone());
                //调用远程服务注册
                R registResult = memberFeignService.regist(userRegisterVo);
                if (registResult.getCode() == 0) {
                    return R.ok();
                } else {
                    return R.error(registResult.getCode(),registResult.getData("msg",new TypeReference<String>(){})).put("data", registResult.getData(new TypeReference<String>(){}));
                }
            } else {
                return R.error(BizCodeEnume.SMS_CODE_EXPIRE.getCode(), BizCodeEnume.SMS_CODE_EXPIRE.getMessage());
            }
        } else {
            return R.error(BizCodeEnume.SMS_CODE_EXPIRE.getCode(), BizCodeEnume.SMS_CODE_EXPIRE.getMessage());
        }
    }

    @PostMapping("/login")
    public R login(@RequestBody UserLoginVo vo) {

        //调用远程服务登录
        R r = memberFeignService.login(vo);
        if (r.getCode() == 0) {
            return R.ok().put("data", r.get("data"));
        } else {
            return R.error(r.getCode(),r.getData("msg",new TypeReference<String>(){})).put("data", r.getData(new TypeReference<String>(){}));

        }
    }
}
