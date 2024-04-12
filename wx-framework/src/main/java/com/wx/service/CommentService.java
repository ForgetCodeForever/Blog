package com.wx.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.ResponseResult;
import com.wx.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2024-03-29 15:59:16
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
