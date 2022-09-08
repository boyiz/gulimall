package com.boyiz.gulimall.auth.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description: 社交用户信息
 **/

@Data
@ToString
public class SocialUser {

    private String access_token;

    private String remind_in;

    private long expires_in;

    private String uid;

    private String isRealName;

}
