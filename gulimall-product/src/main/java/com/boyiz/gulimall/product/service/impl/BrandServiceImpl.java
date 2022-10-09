package com.boyiz.gulimall.product.service.impl;

import com.boyiz.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.Query;

import com.boyiz.gulimall.product.dao.BrandDao;
import com.boyiz.gulimall.product.entity.BrandEntity;
import com.boyiz.gulimall.product.service.BrandService;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //1、获取到key，进行模糊查询
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.eq("brand_id", key).or().like("name", key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetial(BrandEntity brandEntity) {
        //保证冗余字段的数据一致性
        this.updateById(brandEntity);
        if (!StringUtils.isEmpty(brandEntity.getName())) {
            categoryBrandRelationService.updateBrand(brandEntity.getBrandId(), brandEntity.getName());

            //TODO 更新其他关联
        }
    }

}