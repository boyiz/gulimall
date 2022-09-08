package com.boyiz.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 远程调用别的服务
 *      引入openfeign
 *      编写接口，告知springcloud这个接口需要调用远程服务
 *          声明接口的每一个方法是调用的哪个远程服务的哪个请求
 *      开启远程调用功能
 *          @EnableFeignClients
 *              自动扫描@FeignClient下的接口
 *
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.boyiz.gulimall.member.feign")
@EnableDiscoveryClient
public class GulimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }

}
