package com.boyiz.gulimall.product.exception;

import com.boyiz.common.exception.BizCodeEnume;
import com.boyiz.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理异常
 */

@Slf4j

//@ResponseBody
//@ControllerAdvice(basePackages = "com.boyiz.gilimall.product.controller")
//等同于上面两个
@RestControllerAdvice(basePackages = "com.boyiz.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验异常{},异常类型{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((x) -> {
            map.put(x.getField(), x.getDefaultMessage());
        });

        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMessage()).put("data", map);

    }

    @ExceptionHandler(value = Exception.class)
    public R handleException(Throwable throwable) {
        log.error("异常错误：", throwable);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMessage());
    }

}
