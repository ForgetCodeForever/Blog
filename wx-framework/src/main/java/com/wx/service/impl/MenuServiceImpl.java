package com.wx.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.constants.SystemConstants;
import com.wx.entity.Menu;
import com.wx.mapper.MenuMapper;
import com.wx.service.MenuService;
import com.wx.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-04-05 22:40:18
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        // permissions中需要有所有菜单类型为C(菜单)或者F(按钮)的, 状态为正常的, 未被删除的权限
        // 如果是管理员, 返回所有权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menuList = list(wrapper);
            List<String> perms = menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        // 否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;

        // 判断是否是管理员
        // 如果是管理员, 返回所有符合要求的Menu
        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectAllRouterMenu();
        }
        // 否则 返回当前用户具有的Menu
        else {
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        // 构建tree
        List<Menu> menuTree = buildMenuTree(menus, 0L);

        return menuTree;
    }

    // 构建tree -> 递归
    // 先找出第一层的菜单, 然后去找他们的子菜单设置到children属性中
    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    // 获取传入菜单的子菜单
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m0 -> m0.getParentId().equals(menu.getId()))
                .map(m1 -> m1.setChildren(getChildren(m1, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
