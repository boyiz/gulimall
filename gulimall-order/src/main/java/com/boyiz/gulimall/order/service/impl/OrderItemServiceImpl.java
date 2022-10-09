package com.boyiz.gulimall.order.service.impl;

import com.boyiz.gulimall.order.entity.OrderEntity;
import com.boyiz.gulimall.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.Query;

import com.boyiz.gulimall.order.dao.OrderItemDao;
import com.boyiz.gulimall.order.entity.OrderItemEntity;
import com.boyiz.gulimall.order.service.OrderItemService;


@Service("orderItemService")
@RabbitListener(queues = {"hello-java-queue"})
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }



    /**
     * queues：声明需要监听的队列
     * channel：当前传输数据的通道
     *
     * 参数
     *    1、Message message ：原生消息详细信息，头+体
     *    2、T<发送消息的类型> OrderEntity content
     *    3、Channel channel 当前传输数据的通道
     *
     * Queue可以有很多人来监听，只要收到消息，队列就删除消息，且只能有一个收到此消息
     * 场景
     *    1、订单服务启动多个，同一个消息只能被一个客户端收到
     *    2、只有一个消息完全处理完成，方法运行结束，才能接收到下一个消息
     *
     */

    //@RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void revieveMessage(Message message,
                               OrderReturnReasonEntity content,
                               Channel channel) {
        //拿到主体内容
        byte[] body = message.getBody();
        //拿到的消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();
        System.out.println("接受到的消息...内容" + message + "===内容：" + content);

        //channel中按序自增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("deliveryTag ： "+deliveryTag);
        //签收消息，multiple：是否批量签收，非批量签收
        try {
            channel.basicAck(deliveryTag, false);
            //void basicNack(long deliveryTag, boolean multiple, boolean requeue)
            //requeue  是否重新入队，false则丢弃，true 发回服务器重新入队
            //channel.basicNack();

            //void basicReject(long deliveryTag, boolean requeue) throws IOException;
            //不能multiple批量
            //channel.basicReject();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RabbitHandler
    public void revieveMessage(OrderEntity content) {
        System.out.println("接受到的消息..===内容：" + content);

    }

}