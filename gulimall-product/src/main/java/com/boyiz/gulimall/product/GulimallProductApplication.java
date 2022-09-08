package com.boyiz.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1、整合mybatis-plus
 *      1、导入依赖
 *      2、配置
 *          1、配置数据源
 *              1、导入数据库驱动
 *              2、application.yml中配置相关信息
 *          2、配置mybatis-plus
 *              1、使用@MapperScan("com/boyiz/gulimall/product/dao")
 *              2、告诉mybatis-plus，sql映射文件位置
 *
 *
 */
@MapperScan("com/boyiz/gulimall/product/dao")
@SpringBootApplication
@EnableDiscoveryClient
//开启缓存功能
@EnableCaching
//开启远程调用功能
@EnableFeignClients(basePackages = "com.boyiz.gulimall.product.feign")
public class GulimallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }
}
