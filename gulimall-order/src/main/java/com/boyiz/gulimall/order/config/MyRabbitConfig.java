package com.boyiz.gulimall.order.config;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;


@Configuration
public class MyRabbitConfig {

    @Lazy
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 定制 RabbitTemplate
     * 1、设置 spring.rabbitmq.publisher-confirm-type=correlated
     * 2、设置确认回调
     */

//    @PostConstruct  //表示MyRabbitConfig对象创建完成后，执行initRabbitTemplate方法
//    public void initRabbitTemplate() {
//        //设置确认回调
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            /**
//             *
//             * @param correlationData 当前消息的唯一关联数据，可以理解为唯一ID
//             * @param ack  消息是否成功收到
//             * @param cause  失败的原因
//             */
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                System.out.println("" +
//                        "confirm..." +
//                        "correlationData:"+correlationData+
//                        "ack:"+ack+
//                        "cause:"+cause);
//            }
//        });
//    }
}
