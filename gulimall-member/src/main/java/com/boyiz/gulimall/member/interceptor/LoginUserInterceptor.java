package com.boyiz.gulimall.member.interceptor;

import com.boyiz.common.exception.RRException;
import com.boyiz.common.utils.JwtUtil;
import com.boyiz.common.vo.MemberResponseVo;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginUserInterceptor extends HandlerInterceptorAdapter {

    public static ThreadLocal<MemberResponseVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        String uri = request.getRequestURI();
        boolean match = new AntPathMatcher().match("/member/**", uri);
        if (match) {
            return true;
        }
        // 从请求头中获取token字符串
        String jwt = request.getHeader("Authorization");
        // 解析失败就提示用户登录
        Claims claims = JwtUtil.parse(jwt);
        if (claims != null) {
            MemberResponseVo memberResponseVo = new MemberResponseVo();
            memberResponseVo.setId(
                    Long.parseLong(
                            claims.get("sub").toString().split("_")[1]));
            loginUser.set(memberResponseVo);
            return true;
        } else {
            throw new RRException("当前用户未登录");
        }
    }
}