package com.wx.service.impl;

import com.wx.ResponseResult;
import com.wx.constants.SystemConstants;
import com.wx.entity.LoginUser;
import com.wx.entity.User;
import com.wx.service.BlogLoginService;
import com.wx.utils.BeanCopyUtils;
import com.wx.utils.JwtUtil;
import com.wx.utils.RedisCache;
import com.wx.vo.BlogUserLoginVo;
import com.wx.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

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
        redisCache.setCacheObject(SystemConstants.BLOG_LOGIN_PRE + userId, loginUser);

        // 把token 和 userinfo 封装返回
        // 把 user 转换成 userinfovo (bean拷贝)
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        // 获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 获取userid
        Long userId = loginUser.getUser().getId();

        // 删除redis中的用户信息
        redisCache.deleteObject(SystemConstants.BLOG_LOGIN_PRE + userId);
        return ResponseResult.okResult();
    }
}
