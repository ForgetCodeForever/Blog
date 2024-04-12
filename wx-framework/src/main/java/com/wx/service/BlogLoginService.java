package com.wx.service;

import com.wx.ResponseResult;
import com.wx.entity.User;

public interface BlogLoginService {
    public ResponseResult login(User user);

    ResponseResult logout();
}
