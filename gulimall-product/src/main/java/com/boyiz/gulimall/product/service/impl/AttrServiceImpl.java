package com.boyiz.gulimall.product.service.impl;

import com.boyiz.common.constant.ProductConstant;
import com.boyiz.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.boyiz.gulimall.product.dao.AttrGroupDao;
import com.boyiz.gulimall.product.dao.CategoryDao;
import com.boyiz.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.boyiz.gulimall.product.entity.AttrGroupEntity;
import com.boyiz.gulimall.product.entity.CategoryEntity;
import com.boyiz.gulimall.product.service.CategoryService;
import com.boyiz.gulimall.product.vo.AttrGroupRelationVo;
import com.boyiz.gulimall.product.vo.AttrRespVo;
import com.boyiz.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.Query;

import com.boyiz.gulimall.product.dao.AttrDao;
import com.boyiz.gulimall.product.entity.AttrEntity;
import com.boyiz.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")

public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(com.boyiz.gulimall.product.vo.AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity); //将attrVo复制到attrGroupEntity
        //保存基本数据
        this.save(attrEntity);
        //保存关联关系,判断attrType为销售属性时不添加
        if (attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attrVo.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(attrAttrgroupRelationEntity);
        }

    }

    @Override
    public PageUtils queryPageBaseAttr(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.eq("attr_id", key).or().like("attr_name", key);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params), queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> attrRespVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //设置分类和分组
            AttrAttrgroupRelationEntity aare = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (aare != null && aare.getAttrGroupId() != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(aare.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity.getName() != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //分组信息
            AttrAttrgroupRelationEntity aare = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (aare != null) {
                attrRespVo.setAttrGroupId(aare.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(aare.getAttrGroupId());
                if (attrGroupEntity != null) {
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }
        //分类信息
        Long catelogId = attrEntity.getCatelogId();
        attrRespVo.setCatelogPath(categoryService.findCatelogPath(catelogId));
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null) {
            attrRespVo.setCatelogName(categoryEntity.getName());
        }

        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attrVo) {
        //基本修改
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.updateById(attrEntity);
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //修改分组关联
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrVo.getAttrId());
            Long count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (count > 0) {

                relationDao.update(attrAttrgroupRelationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            } else {
                relationDao.insert(attrAttrgroupRelationEntity);
            }
        }


    }

    @Override
    public PageUtils queryPageSaleAttr(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_SALE);
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.eq("attr_id", key).or().like("attr_name", key);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params), queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> attrRespVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            //设置分类和分组
            AttrAttrgroupRelationEntity aare = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (aare != null && aare.getAttrGroupId() != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(aare.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity.getName() != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }

    /**
     * 根据分组   id查询所有关联属性
     *
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> collect = relationEntities.stream().map((attr) -> attr.getAttrId()).collect(Collectors.toList());
        if (collect.size() > 0) {
            Collection<AttrEntity> attrEntities = this.listByIds(collect);
            return (List<AttrEntity>) attrEntities;
        }
        return null;
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        List<AttrAttrgroupRelationEntity> collect = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(collect);
    }

    /**
     * 获取当前分组没有关联的所有属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroup = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroup.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //  1、当前分类下的其他分组
        List<AttrGroupEntity> entities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));
        List<Long> collect = entities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        //  2、这些分组关联的属性
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = relationDao
                .selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        List<Long> longList = attrAttrgroupRelationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        //  3、从前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (longList.size()>0) {
            attrEntityQueryWrapper.notIn("attr_id", longList);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            attrEntityQueryWrapper.eq("attr_id", key).or().like("attr_name", key);
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), attrEntityQueryWrapper);
        return new PageUtils(page);
    }

    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {
        // SELECT attr_id FROM `pms_attr` WHERE attr_id IN(?) AND search_type = 1
        return baseMapper.selectSearchAttrIds(attrIds);
    }


}