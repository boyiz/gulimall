package com.boyiz.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.gulimall.ware.entity.PurchaseEntity;
import com.boyiz.gulimall.ware.vo.MergeVo;
import com.boyiz.gulimall.ware.vo.PurchaseDoneVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:46:08
 */
@Service
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);
}

