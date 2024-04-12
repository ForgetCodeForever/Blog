package com.wx.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.ResponseResult;
import com.wx.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-03-29 16:54:16
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);
}
