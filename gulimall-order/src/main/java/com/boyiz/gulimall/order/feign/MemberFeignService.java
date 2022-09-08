package com.boyiz.gulimall.order.feign;

import com.boyiz.common.utils.R;
import com.boyiz.gulimall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("gulimall-member")
public interface MemberFeignService {

    //查询用户收货地址列表
    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);
}
