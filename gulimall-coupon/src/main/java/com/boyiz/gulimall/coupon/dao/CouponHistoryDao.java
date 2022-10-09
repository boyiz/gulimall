package com.boyiz.gulimall.coupon.dao;

import com.boyiz.gulimall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:23:58
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
