package com.boyiz.gulimall.product.app;

import java.util.Arrays;
import java.util.List;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.boyiz.common.exception.BizCodeEnume;
import com.boyiz.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boyiz.gulimall.product.entity.CategoryEntity;
import com.boyiz.gulimall.product.service.CategoryService;
import com.boyiz.common.utils.R;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品三级分类
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 16:43:13
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类以及子分类，以树形结构组装
     */
    @RequestMapping("/list/tree")
    //@RequiresPermissions("product:category:list")
    public R list(HttpServletRequest request){
        //TODO   TOKEN 验证
        // 从请求头中获取token字符串
        String jwt = request.getHeader("Authorization");
        // 解析失败就提示用户登录
        Claims claims = JwtUtil.parse(jwt);
        if (claims == null) {
            return R.error(BizCodeEnume.USER_NOLOGIN_EXCEPTION.getCode(),BizCodeEnume.USER_NOLOGIN_EXCEPTION.getMessage());
        }
        List<CategoryEntity> list = categoryService.listWithTree();
        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
   // @RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		//categoryService.updateById(category);
		categoryService.updateCascade(category);

        return R.ok();
    }

    /**
     * 删除
     * @RequestBody：获取请求体，需要发送POST请求
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		//categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
