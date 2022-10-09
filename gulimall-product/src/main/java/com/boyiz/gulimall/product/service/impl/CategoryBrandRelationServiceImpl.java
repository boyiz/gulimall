package com.boyiz.gulimall.product.service.impl;

import com.boyiz.gulimall.product.dao.BrandDao;
import com.boyiz.gulimall.product.dao.CategoryDao;
import com.boyiz.gulimall.product.entity.BrandEntity;
import com.boyiz.gulimall.product.entity.CategoryEntity;
import com.boyiz.gulimall.product.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.Query;

import com.boyiz.gulimall.product.dao.CategoryBrandRelationDao;
import com.boyiz.gulimall.product.entity.CategoryBrandRelationEntity;
import com.boyiz.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryBrandRelationDao categoryBrandRelationDao;
    @Autowired
    @Lazy
    BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
        categoryBrandRelation.setBrandId(brandId);
        categoryBrandRelation.setBrandName(name);
        this.update(categoryBrandRelation, new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
    }

    @Override
    public void updateCascade(Long catId, String name) {
        this.baseMapper.updateCascade(catId, name);
    }

    @Override
    public List<BrandEntity> getByCatId(Long catId) {
        List<CategoryBrandRelationEntity> catelogId = categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntity> collect = catelogId.stream().map(item -> {
            Long brandId = item.getBrandId();
            return brandService.getById(brandId);
        }).collect(Collectors.toList());
        return collect;
    }


}