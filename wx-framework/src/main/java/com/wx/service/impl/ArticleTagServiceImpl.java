package com.wx.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.entity.ArticleTag;
import com.wx.mapper.ArticleTagMapper;
import com.wx.service.ArticleTagService;
import org.springframework.stereotype.Service;
/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2024-04-07 21:53:50
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
