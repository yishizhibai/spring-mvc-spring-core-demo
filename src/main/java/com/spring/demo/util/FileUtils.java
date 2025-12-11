package com.spring.demo.util;

import org.springframework.stereotype.Component;
import java.io.File;
import java.util.Arrays;

@Component
public class FileUtils {
    // 自动创建目录（增加空值校验）
    public void createDirIfNotExists(String dirPath) {
        if (dirPath == null || dirPath.trim().isEmpty()) {
            throw new IllegalArgumentException("目录路径不能为空");
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("创建目录失败：" + dirPath);
            }
        }
    }

    // 获取文件类型
    public static String getFileType(String suffix) {
        if (suffix == null) return "未知类型";
        suffix = suffix.toLowerCase();
        if (Arrays.asList("jpg", "png", "gif", "jpeg").contains(suffix)) {
            return "图片";
        } else if (Arrays.asList("doc", "docx", "pdf", "txt").contains(suffix)) {
            return "文档";
        } else {
            return "其他";
        }
    }

    // 格式化文件大小
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2fKB", size / 1024.0);
        } else {
            return String.format("%.2fMB", size / (1024.0 * 1024.0));
        }
    }
}