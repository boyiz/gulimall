package com.boyiz.gulimall.order.dao;

import com.boyiz.gulimall.order.entity.OrderOperateHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单操作历史记录
 * 
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:41:14
 */
@Mapper
public interface OrderOperateHistoryDao extends BaseMapper<OrderOperateHistoryEntity> {
	
}
