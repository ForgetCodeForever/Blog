package com.wx.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.ResponseResult;
import com.wx.dto.TagListDto;
import com.wx.entity.Tag;
import com.wx.vo.PageVo;
import com.wx.vo.TagAdminVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2024-04-05 15:44:39
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    List<TagAdminVo> listAllTag();
}
