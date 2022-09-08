//package com.boyiz.gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//
//@Configuration
//public class Cors_Configuration {
//
//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//
//        //配置跨域
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.setAllowCredentials(true);
//
//        source.registerCorsConfiguration("/", corsConfiguration);
//        return new CorsWebFilter(source);
//    }
//}
