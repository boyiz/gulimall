package com.boyiz.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.boyiz.common.validator.group.AddGroup;
import com.boyiz.common.validator.group.UpdateGroup;
import com.boyiz.common.validator.group.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boyiz.gulimall.product.entity.BrandEntity;
import com.boyiz.gulimall.product.service.BrandService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.R;


/**
 * 品牌
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 16:43:13
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
   // @RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("product:brand:save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand /*, BindingResult result*/){
//        if (result.hasErrors()) {
//            Map<String, String> map = new HashMap<>();
//            //1、获取校验的错误结果
//            result.getFieldErrors().forEach((x) -> {
//                //fieldError获取错误提示
//                String message = x.getDefaultMessage();
//                //获取错误的属性名
//                String field = x.getField();
//                map.put(field, message);
//            });
//            return R.error(400, "数据不合法").put("data", map);
//        } else {
//            brandService.save(brand);
//        }
        brandService.save(brand);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated({UpdateGroup.class}) @RequestBody BrandEntity brand){
		brandService.updateById(brand);
        brandService.updateDetial(brand);

        return R.ok();
    }
    /**
     * 修改显示状态
     */
    @RequestMapping("/update/status")
    //@RequiresPermissions("product:brand:update")
    public R updateStatus(@Validated({UpdateStatusGroup.class}) @RequestBody BrandEntity brand){
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
