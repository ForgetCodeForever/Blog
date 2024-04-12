package com.wx.controller;

import com.wx.ResponseResult;
import com.wx.dto.AddTagDto;
import com.wx.dto.TagListDto;
import com.wx.entity.Tag;
import com.wx.service.TagService;
import com.wx.utils.BeanCopyUtils;
import com.wx.vo.PageVo;
import com.wx.vo.TagAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;


    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto) {
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        return tagService.addTag(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagAdminVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}