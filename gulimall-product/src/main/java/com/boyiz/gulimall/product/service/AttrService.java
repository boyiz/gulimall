package com.boyiz.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.product.entity.AttrEntity;
import com.boyiz.gulimall.product.vo.AttrGroupRelationVo;
import com.boyiz.gulimall.product.vo.AttrRespVo;
import com.boyiz.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 16:14:05
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(com.boyiz.gulimall.product.vo.AttrVo attrVo);

    PageUtils queryPageBaseAttr(Map<String, Object> params, Long catelogId);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attrVo);

    PageUtils queryPageSaleAttr(Map<String, Object> params, Long catelogId);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的所有属性集合中，挑出检索属性
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

