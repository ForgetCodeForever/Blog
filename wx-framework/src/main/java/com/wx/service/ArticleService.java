package com.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.ResponseResult;
import com.wx.dto.AddArticleDto;
import com.wx.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);
}
