package com.boyiz.gulimall.order.config;

import com.boyiz.gulimall.order.interceptor.LoginUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OrderWebConfiguration implements WebMvcConfigurer {

    @Autowired
    LoginUserInterceptor loginUserInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**");
    }
}
