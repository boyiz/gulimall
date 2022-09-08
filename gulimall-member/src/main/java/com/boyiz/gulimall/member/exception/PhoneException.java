package com.boyiz.gulimall.member.exception;


public class PhoneException extends RuntimeException {

    public PhoneException() {
        super("手机号已存在");
    }
}
