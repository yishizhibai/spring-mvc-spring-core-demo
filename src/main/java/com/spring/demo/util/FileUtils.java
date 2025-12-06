package com.spring.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    // 允许的文件格式
    private static final String[] ALLOWED_SUFFIX = {"txt", "pdf", "docx", "png", "jpg"};
    // 文件存储路径（从配置文件读取）
    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 校验文件格式
     */
    public boolean checkFileSuffix(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }
        // 获取文件后缀
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return false;
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        // 校验后缀
        for (String s : ALLOWED_SUFFIX) {
            if (s.equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件类型（文档/图片）
     */
    public String getFileType(String suffix) {
        if ("png".equals(suffix) || "jpg".equals(suffix)) {
            return "图片";
        } else if ("txt".equals(suffix) || "pdf".equals(suffix) || "docx".equals(suffix)) {
            return "文档";
        }
        return "其他";
    }

    /**
     * 格式化文件大小（字节转MB/KB）
     */
    public String formatFileSize(Long size) {
        if (size == null) {
            return "0B";
        }
        // 1MB = 1024*1024 B
        if (size >= 1024 * 1024) {
            return String.format("%.2fMB", (double) size / (1024 * 1024));
        } else if (size >= 1024) {
            return String.format("%.2fKB", (double) size / 1024);
        } else {
            return size + "B";
        }
    }

    /**
     * 保存文件到指定路径
     */
    public String saveFile(MultipartFile file) throws IOException {
        // 创建存储目录（不存在则创建）
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            log.info("创建上传目录：{}，结果：{}", uploadPath, mkdirs);
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        String fileId = UUID.randomUUID().toString().replace("-", "");
        String newFileName = fileId + "." + suffix;
        Path filePath = Paths.get(uploadPath, newFileName);

        // 保存文件
        Files.write(filePath, file.getBytes());
        log.info("文件保存成功：{}", filePath.toString());
        return filePath.toString();
    }

    /**
     * 获取纯文件名（无后缀）
     */
    public String getPureFileName(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return originalFilename;
        }
        return originalFilename.substring(0, originalFilename.lastIndexOf("."));
    }
}