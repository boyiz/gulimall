package com.boyiz.gulimall.order.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.boyiz.common.exception.BizCodeEnume;
import com.boyiz.common.exception.RRException;
import com.boyiz.common.exception.RRExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.boyiz.gulimall.order.entity.OrderEntity;
import com.boyiz.gulimall.order.service.OrderService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.R;



/**
 * 订单
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:41:14
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 分页查询当前登录用户的所有订单信息
     * @param params
     * @return
     */
    @PostMapping("/listWithItem")
    //@RequiresPermissions("order:order:list")
    public R listWithItem(@RequestBody Map<String, Object> params){
        PageUtils page = orderService.queryPageWithItem(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/status/{orderSn}")
    public R getOrderStatus(@PathVariable("orderSn") String orderSn) {
        OrderEntity order = orderService.getOrderByOrderSn(orderSn);
        return R.ok().setData(order);
    }
    /**
     * 列表
     */
    @ExceptionHandler(value = RRException.class)
    @RequestMapping("/list")
    //@RequiresPermissions("order:order:list")
    public R list(@RequestParam Map<String, Object> params) throws RRExceptionHandler{
        PageUtils page = orderService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("order:order:info")
    public R info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("order:order:save")
    public R save(@RequestBody OrderEntity order){
		orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("order:order:update")
    public R update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("order:order:delete")
    public R delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @ExceptionHandler(value = RRException.class)
    public R error(RRException e) {
        return R.error(BizCodeEnume.USER_NOLOGIN_EXCEPTION.getCode(), BizCodeEnume.USER_NOLOGIN_EXCEPTION.getMessage());
    }

}
