package com.boyiz.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.boyiz.common.exception.BizCodeEnume;
import com.boyiz.gulimall.member.exception.PhoneException;
import com.boyiz.gulimall.member.exception.UsernameException;
import com.boyiz.gulimall.member.feign.CouponFeignService;
import com.boyiz.gulimall.member.vo.MemberUserLoginVo;
import com.boyiz.gulimall.member.vo.MemberUserRegisterVo;
import com.boyiz.gulimall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.boyiz.gulimall.member.entity.MemberEntity;
import com.boyiz.gulimall.member.service.MemberService;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.R;


/**
 * 会员
 *
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 17:31:41
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;

    @PostMapping("/oauth2/login")
    public R authLogin(@RequestBody SocialUser socialUser) throws Exception  {
        MemberEntity entity = memberService.login(socialUser);
        if (entity != null) {
            return R.ok().put("data", entity);
        } else {
            return R.error(BizCodeEnume.USER_AUTHLOGIN_EXCEPTION.getCode(),BizCodeEnume.USER_AUTHLOGIN_EXCEPTION.getMessage());
        }
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberUserLoginVo loginVo) {
        MemberEntity entity = memberService.login(loginVo);

        if (entity != null) {
            return R.ok().put("data", entity);
        } else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getMessage());
        }
    }

    @PostMapping("/regist")
    public R regist(@RequestBody MemberUserRegisterVo memberUserRegisterVo) {
        try {
            memberService.regist(memberUserRegisterVo);
        } catch (PhoneException e) {
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnume.PHONE_EXIST_EXCEPTION.getMessage());
        } catch (UsernameException e) {
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(), BizCodeEnume.USER_EXIST_EXCEPTION.getMessage());
        }
        return R.ok();
    }

    @RequestMapping("/coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("ZS");
        R membercoupons = couponFeignService.membercoupon();
        return R.ok().put("member", memberEntity).put("coupons", membercoupons.get("coupon"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
