package com.boyiz.gulimall.ware.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;


@Configuration
public class MyRabbitMQConfig {

    @Lazy
    @Autowired
    RabbitTemplate rabbitTemplate;

//    @RabbitListener(queues = "stock.release.stock.queue")
//    public void handle(Message message) {
//
//    }
    /**
     * 使用JSON序列化机制，进行消息转换
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     库存服务默认的交换机
     **/
    @Bean
    public Exchange stockEvenExchange() {
       return new TopicExchange("stock-even-exchange", true, false);
    }

    /**
     * 普通队列
     * @return
     */
    @Bean
    public Queue stockReleaseStockQueue() {
        //String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        Queue queue = new Queue("stock.release.stock.queue", true, false, false);
        return queue;
    }

    /**
     * 延迟队列
     * @return
     */
    @Bean
    public Queue stockDelay() {

        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "stock-event-exchange");
        arguments.put("x-dead-letter-routing-key", "stock.release");
        // 消息过期时间 2分钟
        arguments.put("x-message-ttl", 120000);
        Queue queue = new Queue("stock.delay.queue", true, false, false,arguments);
        return queue;
    }

    /**
     * 交换机与普通队列绑定
     * @return
     */
    @Bean
    public Binding stockReleaseLocked() {
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        // 			Map<String, Object> arguments
        Binding binding = new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.release.#",
                null);

        return binding;
    }
    /**
     * 交换机与延迟队列绑定
     * @return
     */
    @Bean
    public Binding stockLockedBinding() {
        return new Binding(
                "stock.delay.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.locked",
                null);
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
