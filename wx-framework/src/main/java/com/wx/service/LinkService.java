package com.wx.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.ResponseResult;
import com.wx.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-03-23 22:29:49
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}
