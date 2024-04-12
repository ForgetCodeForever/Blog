package com.wx.controller;

import com.wx.ResponseResult;
import com.wx.constants.SystemConstants;
import com.wx.entity.LoginUser;
import com.wx.entity.Menu;
import com.wx.entity.User;
import com.wx.enums.AppHttpCodeEnum;
import com.wx.exception.SystemException;
import com.wx.service.MenuService;
import com.wx.service.RoleService;
import com.wx.service.SystemLoginService;
import com.wx.utils.BeanCopyUtils;
import com.wx.utils.RedisCache;
import com.wx.utils.SecurityUtils;
import com.wx.vo.AdminUserInfoVo;
import com.wx.vo.RoutersVo;
import com.wx.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private SystemLoginService systemLoginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())) {
            // 提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return systemLoginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        // {"code", "data{""permissions", "roles", "user"}, "msg"}
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();

        // 根据用户ID查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());

        // 根据用户ID查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        // 获取用户信息-为封装数据第三个参数服务
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        // 封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }


    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        Long userId = SecurityUtils.getUserId();

        // 查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);

        // 封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return systemLoginService.logout();
    }
}
