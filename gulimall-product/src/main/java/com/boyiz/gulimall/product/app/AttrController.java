package com.boyiz.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.boyiz.gulimall.product.entity.ProductAttrValueEntity;
import com.boyiz.gulimall.product.service.ProductAttrValueService;
import com.boyiz.gulimall.product.vo.AttrRespVo;
import com.boyiz.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.boyiz.gulimall.product.service.AttrService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.R;


/**
 * 商品属性
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 16:43:13
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    ProductAttrValueService productAttrValueService;

    // /product/attr/base/listforspu/{spuId}
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrlistforspu(@PathVariable("spuId") Long spuId){

        List<ProductAttrValueEntity> entities = productAttrValueService.baseAttrlistforspu(spuId);

        return R.ok().put("data",entities);
    }


    //平台属性-》规格参数
    @GetMapping("/base/list/{catelogId}")
    public R baseAttrList(@PathVariable("catelogId") Long catelogId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPageBaseAttr(params, catelogId);
        return R.ok().put("data", page);
    }
    //平台属性-》销售属性
    @GetMapping("/sale/list/{catelogId}")
    public R saleAttrList(@PathVariable("catelogId") Long catelogId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPageSaleAttr(params, catelogId);
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    // @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
        //AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);
        return R.ok().put("data", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attrVo) {
        attrService.saveAttr(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attrVo) {
        //attrService.updateById(attr);
        attrService.updateAttr(attrVo);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
