package com.wx.utils;

import com.wx.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    /**
    * 获取用户
    **/
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            return (LoginUser) authentication.getPrincipal();
        }
        return null; // 或者根据业务需求返回适当的默认值
    }



    /**
    * 获取Authentication
    */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public static Boolean isAdmin() {
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }


    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            return loginUser.getUser().getId();
        }
        return null; // 或者根据业务需求返回适当的默认值
    }
}