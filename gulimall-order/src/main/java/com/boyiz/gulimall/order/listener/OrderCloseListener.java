package com.boyiz.gulimall.order.listener;

import com.boyiz.gulimall.order.entity.OrderEntity;
import com.boyiz.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName OrderCloseListener
 * @Description TODO
 * @Author boyiz
 * @Date 2022/8/20 19:41
 * @Version 1.0
 **/
@RabbitListener(queues ="order.release.order.queue")
@Service
public class OrderCloseListener {
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity orderEntity, Channel channel, Message message) throws IOException {
        System.out.println("收到过期的订单信息，准备关闭订单" + orderEntity.getOrderSn());
        try {
            orderService.closeOrder(orderEntity);
            //手动关闭支付宝收单
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            //重回消息队列
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }
}
