package com.wx.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.ResponseResult;
import com.wx.constants.SystemConstants;
import com.wx.entity.Article;
import com.wx.entity.Category;
import com.wx.mapper.CategoryMapper;
import com.wx.service.ArticleService;
import com.wx.service.CategoryService;
import com.wx.utils.BeanCopyUtils;
import com.wx.vo.CategoryAdminVo;
import com.wx.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2024-03-23 14:20:39
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        /**
         * 需求: 页面上需要展示分类列表, 用户可以点击具体的分类查看该分类下的文章列表
         * 注意:
         * 1. 要求只展示有发布正式文章的分类
         * 2. 必须是正常状态的分类(分类表 String status == 0)
         */
        // 查询文章表, 状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        // 获取文章的分类id, 并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        // 查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        // 封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryAdminVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryAdminVo> categoryAdminVos = BeanCopyUtils.copyBeanList(list, CategoryAdminVo.class);
        return categoryAdminVos;
    }
}
