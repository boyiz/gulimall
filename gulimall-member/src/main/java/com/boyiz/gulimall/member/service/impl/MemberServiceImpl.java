package com.boyiz.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boyiz.common.utils.HttpUtils;
import com.boyiz.gulimall.member.dao.MemberLevelDao;
import com.boyiz.gulimall.member.entity.MemberLevelEntity;
import com.boyiz.gulimall.member.exception.PhoneException;
import com.boyiz.gulimall.member.exception.UsernameException;
import com.boyiz.gulimall.member.service.MemberLevelService;
import com.boyiz.common.utils.JwtUtil;

import com.boyiz.gulimall.member.vo.MemberUserLoginVo;
import com.boyiz.gulimall.member.vo.MemberUserRegisterVo;
import com.boyiz.gulimall.member.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.Query;

import com.boyiz.gulimall.member.dao.MemberDao;
import com.boyiz.gulimall.member.entity.MemberEntity;
import com.boyiz.gulimall.member.service.MemberService;
import org.springframework.transaction.annotation.Transactional;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void regist(MemberUserRegisterVo memberUserRegisterVo) {
        MemberEntity memberEntity = new MemberEntity();

        //设置默认会员等级
        MemberLevelEntity levelEntity = memberLevelDao.selectOne(new QueryWrapper<MemberLevelEntity>().eq("default_status", 1));
        memberEntity.setLevelId(levelEntity.getId());
        //检查手机号和用户名是否唯一
        //使用异常机制
        checkPhoneUnique(memberUserRegisterVo.getPhone());
        checkUsernameUnique(memberUserRegisterVo.getUserName());

        memberEntity.setUsername(memberUserRegisterVo.getUserName());
        memberEntity.setMobile(memberUserRegisterVo.getPhone());
        //设置密码,加密存储
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(memberUserRegisterVo.getPassword());
        memberEntity.setPassword(encode);
        //其他默认信息
        memberEntity.setGender(0);
        memberEntity.setCreateTime(new Date());
        //保存
        baseMapper.insert(memberEntity);

    }

    @Override
    public void checkPhoneUnique(String Phone) throws PhoneException {
        Integer count = Math.toIntExact(baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", Phone)));
        if (count > 0) {
            throw new PhoneException();
        }
    }

    @Override
    public void checkUsernameUnique(String UserName) throws UsernameException {
        Integer count = Math.toIntExact(baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", UserName)));
        if (count > 0) {
            throw new UsernameException();
        }

    }

    @Override
    public MemberEntity login(MemberUserLoginVo loginVo) {
        String loginAcct = loginVo.getLoginacct();
        String password = loginVo.getPassword();

        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", loginAcct)
                .or()
                .eq("mobile", loginAcct));
        if (memberEntity == null) {
            return null;
        } else {
            //密码匹配
            boolean matches = new BCryptPasswordEncoder().matches(password, memberEntity.getPassword());
            if (matches) {
                memberEntity.setPassword("");
                //返回jwt
                memberEntity.setToken(JwtUtil.generate(memberEntity.getUsername()+"_"+memberEntity.getId()));
                return memberEntity;
            } else {
                return null;
            }
        }

    }

    /**
     * 社交登录，包含登录和注册功能
     *
     * @param socialUser
     * @return
     */
    @Transactional
    @Override
    public MemberEntity login(SocialUser socialUser) throws Exception {

        String uid = socialUser.getUid();
        //1、判断当前社交用户是否已经登录过系统
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));
        if (memberEntity != null) {
            //这个用户已经注册过
            //更新用户的访问令牌的时间和access_token
            //TODO：考虑AccessToken和ExpiresIn可以放入Redis中，同时社交登录考虑分表
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(socialUser.getAccess_token());
            update.setExpiresIn(socialUser.getExpires_in());
            this.baseMapper.updateById(update);
            memberEntity.setAccessToken(socialUser.getAccess_token());
            memberEntity.setExpiresIn(socialUser.getExpires_in());
            //返回jwt
            memberEntity.setToken(JwtUtil.generate(memberEntity.getUsername()+"_"+memberEntity.getId()));
            memberEntity.setPassword("");
            return memberEntity;
        } else {
            //2、没有查到当前社交用户对应的记录我们就需要注册一个
            MemberEntity register = new MemberEntity();
            try {
                //3、查询当前社交用户的社交账号信息（昵称、性别等）
                Map<String, String> query = new HashMap<>();
                query.put("access_token", socialUser.getAccess_token());
                query.put("uid", socialUser.getUid());
                HttpResponse response = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<String, String>(), query);

                if (response.getStatusLine().getStatusCode() == 200) {
                    //查询成功
                    String json = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(json);
                    String nickName = jsonObject.getString("name");
                    String gender = jsonObject.getString("gender");
                    String profileImageUrl = jsonObject.getString("profile_image_url");

                    register.setNickname(nickName);
                    register.setUsername(nickName);
                    register.setGender("m".equals(gender) ? 1 : 0);
                    register.setHeader(profileImageUrl);
                    register.setCreateTime(new Date());
                }
            } catch (Exception e) {
            }
            register.setSocialUid(socialUser.getUid());
            register.setAccessToken(socialUser.getAccess_token());
            register.setExpiresIn(socialUser.getExpires_in());

            //把用户信息插入到数据库中
            this.baseMapper.insert(register);
            register.setPassword("");
            //返回jwt
            memberEntity.setToken(JwtUtil.generate(memberEntity.getUsername()+"_"+memberEntity.getId()));
            return register;
        }
    }
}