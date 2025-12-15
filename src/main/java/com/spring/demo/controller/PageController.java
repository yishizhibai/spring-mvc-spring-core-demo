package com.spring.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面跳转控制器（返回Thymeleaf模板）
 */
@Controller
public class PageController {

    /**
     * 访问文件管理页面
     */
    @GetMapping("/file/index")
    public String fileIndex() {
        // 对应templates/file/index.html
        return "file/index";
    }
}