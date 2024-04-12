package com.wx.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.ResponseResult;
import com.wx.entity.Category;
import com.wx.vo.CategoryAdminVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-03-23 14:20:39
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryAdminVo> listAllCategory();
}
