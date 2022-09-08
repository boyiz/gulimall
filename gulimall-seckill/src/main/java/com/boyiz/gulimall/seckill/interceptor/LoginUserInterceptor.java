package com.boyiz.gulimall.seckill.interceptor;

import com.boyiz.common.exception.RRException;
import com.boyiz.common.utils.JwtUtil;
import com.boyiz.common.vo.MemberResponseVo;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * @Description: 登录拦截器
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-02 18:37
 **/

@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberResponseVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/seckill/kill/**", uri);
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

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
