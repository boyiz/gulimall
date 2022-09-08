package com.boyiz.gulimall.order.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

@Configuration
public class InitRabbitTemplate {


    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 定制 RabbitTemplate
     * 1、服务器收到消息就回调
     *      1、设置 spring.rabbitmq.publisher-confirm-type=correlated
     *      2、设置确认回调 rabbitTemplate.setConfirmCallback
     * 2、消息抵达队列
     *      1、设置
     *          spring.rabbitmq.publisher-returns=true
     *          spring.rabbitmq.template.mandatory=true
     *      2、设置确认回调 rabbitTemplate.setReturnsCallback
     * 3、消费端消息确认（保证每条消息被正确消费，此时才可以删除这个消息）
     *      1、默认自动确认，只要消息接收到，客户端会自动确认，服务器就会删除这个消息
     *          问题：
     *              收到很多消息，自动回复给服务器ack，此时只有一个消息处理完成，发生宕机，会导致消息丢失
     *            需要手动确认：只要没告知MQ被签收，没有ack，消息就一直是unacked状态，即使consumer宕机，
     *                      消息也不会丢失，会变为Ready，下一次有新的consumer连接就将消息发给他
     *      2、如何签收
     *          配置 spring.rabbitmq.listener.simple.acknowledge-mode=manual
     *          签收：
     *              channel.basicAck(); 成功完成则签收
     *          拒绝签收：
     *              channel.basicNack(); 业务失败拒签
     *              channel.basicReject();
     *
     */

    @PostConstruct  //表示MyRabbitConfig对象创建完成后，执行initRabbitTemplate方法
    public void initRabbitTemplate() {
        //消息抵达Brock的确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 当前消息的唯一关联数据，可以理解为唯一ID
             * @param ack  消息是否成功收到
             * @param cause  失败的原因
             *
             *    只要消息抵达Brock，ack = true
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("" +
                        "confirm..." +
                        "  correlationData:" + correlationData +
                        "  ack:" + ack +
                        "  cause:" + cause);
            }
        });
        //消息抵达队列的确认回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            /**
             * 只要消息没有到达指定队列，触发这个失败回调
             * @param returned
             * ReturnedMessage
             * 	    private final Message message;   投递失败的消息详细信息
             * 	    private final int replyCode;     失败回复的状态码
             * 	    private final String replyText;  失败回复的文本
             * 	    private final String exchange;   要发给哪个交换机
             * 	    private final String routingKey; 要使用的路由键
             */
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                System.out.println("Fail Message" +
                        "  message:"+returned.getMessage()+
                        "  replyCode:"+returned.getReplyCode()+
                        "  replyText:"+returned.getReplyText()+
                        "  exchange:"+returned.getExchange()+
                        "  routingKey:"+returned.getRoutingKey()
                        );
            }
        });
    }
}
