package com.boyiz.gulimall.product.dao;

import com.boyiz.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 16:14:05
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
