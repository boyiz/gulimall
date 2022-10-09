package com.boyiz.gulimall.order.dao;

import com.boyiz.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:41:14
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
