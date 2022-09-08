package com.boyiz.gulimall.member.exception;

public class UsernameException extends RuntimeException {


    public UsernameException() {
        super("用户名已存在");
    }
}
