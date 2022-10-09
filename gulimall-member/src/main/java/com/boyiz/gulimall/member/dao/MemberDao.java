package com.boyiz.gulimall.member.dao;

import com.boyiz.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:31:41
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
