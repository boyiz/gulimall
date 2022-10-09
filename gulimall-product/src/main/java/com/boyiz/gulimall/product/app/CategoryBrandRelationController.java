package com.boyiz.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boyiz.gulimall.product.entity.BrandEntity;
import com.boyiz.gulimall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.boyiz.gulimall.product.entity.CategoryBrandRelationEntity;
import com.boyiz.gulimall.product.service.CategoryBrandRelationService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 16:43:13
 */
@RestController
@RequestMapping("product/categorybrandrelation")

public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 获取分类下所有品牌
     * http://localhost:88/api/product/categorybrandrelation/brands/list?t=1659087054692&catId=225
     */
    @GetMapping("/brands/list")
    public R brandRelationList(@RequestParam(value = "catId",required = true) Long catId) {
        List<BrandEntity> vos = categoryBrandRelationService.getByCatId(catId);
        List<BrandVo> collect = vos.stream().map((brandEntity) -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandName(brandEntity.getName());
            brandVo.setBrandId(brandEntity.getBrandId());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data", collect);
    }



    /**
     * 列表
     */
    //@RequestMapping(value = "catelog/list",method = RequestMethod.GET)
    @GetMapping("/catelog/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R catelogList(@RequestParam Long brandId){
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId)
        );
        return R.ok().put("data", list);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
