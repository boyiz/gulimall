package com.boyiz.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.boyiz.common.utils.R;
import com.boyiz.gulimall.ware.feign.MemberFeignService;
import com.boyiz.gulimall.ware.vo.FareVo;
import com.boyiz.gulimall.ware.vo.MemberAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.Query;

import com.boyiz.gulimall.ware.dao.WareInfoDao;
import com.boyiz.gulimall.ware.entity.WareInfoEntity;
import com.boyiz.gulimall.ware.service.WareInfoService;
import org.springframework.util.StringUtils;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Autowired
    MemberFeignService memberFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> wareInfoEntityQueryWrapper = new QueryWrapper<WareInfoEntity>();
        String key = (String) params.get("key");

        if (!StringUtils.isEmpty(key)) {
            wareInfoEntityQueryWrapper
                    .eq("id", key)
                    .or().like("name", key)
                    .or().like("address", key)
                    .or().like("areacode", key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wareInfoEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public FareVo getFare(Long id) {
        FareVo fareVo = new FareVo();
        R info = memberFeignService.info(id);
        MemberAddressVo data = info.getData("memberReceiveAddress",new TypeReference<MemberAddressVo>() {
        });
        if (data != null) {
            //TODO 接入快递服务
//            new BigDecimal("10");
            fareVo.setAddress(data);
            fareVo.setFare(BigDecimal.valueOf(Math.random()*20));
            return fareVo;
        }
        return null;
    }

}