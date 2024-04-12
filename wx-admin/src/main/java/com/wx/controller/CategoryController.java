package com.wx.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson.JSON;
import com.wx.ResponseResult;
import com.wx.entity.Category;
import com.wx.enums.AppHttpCodeEnum;
import com.wx.service.CategoryService;
import com.wx.utils.BeanCopyUtils;
import com.wx.utils.WebUtils;
import com.wx.vo.CategoryAdminVo;
import com.wx.vo.CategoryVo;
import com.wx.vo.ExcelCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryAdminVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }


    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //获取需要导出的数据
            List<Category> categories = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);

            //把数据写入到Excel中
            ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class);

            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);

            excelWriterBuilder
                    .autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

}
