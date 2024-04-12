package com.wx.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-04-05 22:48:18
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
