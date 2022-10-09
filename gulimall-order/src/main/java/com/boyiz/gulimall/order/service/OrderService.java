package com.boyiz.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.to.mq.SeckillOrderTo;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.order.entity.OrderEntity;
import com.boyiz.gulimall.order.vo.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:41:14
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo);

    OrderEntity getOrderByOrderSn(String orderSn);

    void closeOrder(OrderEntity orderEntity);

    PayVo getOrderPay(String orderSn);

    PageUtils queryPageWithItem(Map<String, Object> params);

    String handlePayResult(PayAsyncVo asyncVo);

    String asyncNotify(String notifyData);

    /**
     * 创建秒杀单
     * @param orderTo
     */
    void createSeckillOrder(SeckillOrderTo orderTo);
}

