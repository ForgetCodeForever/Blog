package com.wx.service.impl;

import com.wx.ResponseResult;
import com.wx.constants.SystemConstants;
import com.wx.entity.LoginUser;
import com.wx.entity.User;
import com.wx.service.SystemLoginService;
import com.wx.utils.JwtUtil;
import com.wx.utils.RedisCache;
import com.wx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements SystemLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        // 把用户信息存入redis
        redisCache.setCacheObject(SystemConstants.ADMIN_LOGIN_PRE + userId, loginUser);

        // 把token封装 返回
        Map<String,String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        // 获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
        // 删除redis中对应的值
        redisCache.deleteObject(SystemConstants.ADMIN_LOGIN_PRE + userId);
        return ResponseResult.okResult();
    }

}
