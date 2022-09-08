package com.boyiz.gulimall.order.controller;

import com.boyiz.gulimall.order.entity.OrderEntity;
import com.boyiz.gulimall.order.entity.OrderReturnReasonEntity;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class RabbitController {


    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendq")
    public String senMQ(@RequestParam(value = "num", defaultValue = "10") Integer num) {
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setId(1L);
                orderEntity.setNote("啊啊啊-" + i);
                orderEntity.setCreateTime(new Date());
                rabbitTemplate.convertAndSend(
                        "hello-java-direct-exchange",
                        "hello.java",
                        orderEntity,
                        new CorrelationData(UUID.randomUUID().toString()));
            } else {
                OrderReturnReasonEntity orderReturnReasonEntity = new OrderReturnReasonEntity();
                orderReturnReasonEntity.setName(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend(
                        "hello-java-direct-exchange",
                        "hello.java",
                        orderReturnReasonEntity,
                        new CorrelationData(UUID.randomUUID().toString()));
            }
        }
        return "ok";
    }
}
