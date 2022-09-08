package com.boyiz.gulimall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 使用RabbitMQ
 * 1、导入依赖，引入amqp场景，RabbitAutoConfiguration就会自动生效
 *
 * 2、给容器中自动配置
 *      RabbitTemplate、AmqpAdmin、RabbitMessagingTemplate、CachingConnectionFactory
 *          所有配置属性都是
 *          @ConfigurationProperties(prefix = "spring.rabbitmq")
 *      在配置文件中配置 spring.rabbitmq.xxx 信息
 * 3、开启RabbitMQ，@EnableRabbit
 * 4、监听消息使用 @RabbitListener，必须开启@EnableRabbit
 *      @RabbitListener：可以标柱在类或方法上，标柱在类上，表示监听哪些队列
 *      @RabbitHandler：只能标柱在方法上，标柱在方法上，重载区分不同的消息
 *
 *
 * 本地事务失效问题
 * 同一个对象内事务方法互调默认失效，原因：绕过了代理对象，事务使用代理对象来控制的
 * 解决：使用代理对象来调用事务方法
 *  1、引入starter-aop，它帮我们引入来aspectj
 *          <dependency>
 *             <groupId>org.springframework.boot</groupId>
 *             <artifactId>spring-boot-starter-aop</artifactId>
 *         </dependency>
 *  2、开启 aspectj 动态代理功能 @EnableAspectJAutoProxy
 *      以后所有的动态代理都是aspectj创建的，（即使没有接口也可以创建动态代理）
 *      exposeProxy = true 对外暴露代理对象
 *  3、使用代理对象本类互调
 *
 *
 * Seata控制分布式事务
 *  1、为每一个微服务的数据库创建一张 undo_log 表
 *  2、安装事务协调器 seata-server  https://github.com/seata/seata/releases
 *  3、整合
 *      1)导入依赖 spring-cloud-starter-alibaba-seata  seata-all 0.7.1
 *      2)解压启动seata-server
 *          registry.conf 注册中心配置， 修改registry type = nacos
 *      3)所有想要用分布式事务的微服务，都应该使用seats DataSourceProxy 代理数据源
 *      4)并且导入  file.conf  registry.conf
 *          修改file.conf vgroup_mapping.{application.name}-fescar-service-group = "default"
 *      5)给分布式大事务的入口标柱@GlobalTransactional
 *          给每一个远程小事务用@Transactional
 *
 *
 */


@EnableAspectJAutoProxy(exposeProxy = true)
@EnableRabbit
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
