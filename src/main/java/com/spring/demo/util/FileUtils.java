package com.spring.demo.util;

import org.springframework.stereotype.Component;

/**
 * 文件工具类
 */
@Component // 若需要注入，加@Component；否则改为private构造+static方法
public class FileUtils {

    // 静态方法：根据文件后缀获取文件类型
    public static String getFileType(String suffix) {
        switch (suffix.toLowerCase()) {
            case "jpg":
            case "png":
            case "gif":
                return "图片";
            case "doc":
            case "docx":
                return "文档";
            case "mp4":
            case "avi":
                return "视频";
            default:
                return "其他";
        }
    }
}