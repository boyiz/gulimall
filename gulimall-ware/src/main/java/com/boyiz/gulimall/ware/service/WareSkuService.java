package com.boyiz.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.to.OrderTo;
import com.boyiz.common.to.mq.StockLockedTo;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.ware.entity.WareSkuEntity;
import com.boyiz.gulimall.ware.vo.SkuHasStockVo;
import com.boyiz.gulimall.ware.vo.WareSkuLockVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:46:08
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo to);
}

