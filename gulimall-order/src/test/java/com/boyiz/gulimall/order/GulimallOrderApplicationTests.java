package com.boyiz.gulimall.order;

import com.boyiz.gulimall.order.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;


@Slf4j
@SpringBootTest
class GulimallOrderApplicationTests {

    /**
     * 1、如何创建Exchange、Queue、Binding
     * 1）使用AmqpAdmin进行创建
     * 2、如何收发消息
     */

    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void createExchange() {
        //全参构造
        //public DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
        DirectExchange directExchange = new DirectExchange("hello-java-direct-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        log.error("DirectExchange[{}]创建成功", "hello-java-direct-exchange");
    }

    @Test
    void createQueue() {
        //public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments)
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        log.error("Queue[{}]创建成功", "hello-java-queue");
    }

    @Test
    void createBinding() {
        //	public Binding(String destination, DestinationType destinationType, String exchange, String routingKey, @Nullable Map<String, Object> arguments)
        //destination：目的地 ， destinationType：目的地类型 ， exchange：交换机 ， routingKey：路由键  ， arguments：参数
        //将exchange交换机 和 destination目的地进行绑定，使用routingKey作为指定路由键
        Binding binding = new Binding(
                "hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello-java-direct-exchange",
                "hello.java",
                null);//
        amqpAdmin.declareBinding(binding);
        log.error("Binding[{}]创建成功", "hello-java-binding");

    }

    @Test
    void sendMessageTest() {
//        rabbitTemplate.convertAndSend(
//                "hello-java-direct-exchange",
//                "hello.java",
//                "Hello World");
//        log.error("消息发送完成");
        //如果发送的消息是一个对象，则会使用序列化机制将对象写出，所以对象必须实现 Serializable 接口
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCreateTime(new Date());
        rabbitTemplate.convertAndSend(
                "hello-java-direct-exchange",
                "hello.java",
                orderEntity);
        log.error("消息发送完成");
        //将消息转为json
        //设置MessageConverter 消息转换器 ————》MyRabbitConfig
    }

    @Test
    void contextLoads() {
    }

}
