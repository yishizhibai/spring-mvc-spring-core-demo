package com.spring.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;

@Component
public class FileChunkUtils {
    private static final Logger log = LoggerFactory.getLogger(FileChunkUtils.class);

    @Value("${file.upload.chunk.temp-path}")
    private String chunkTempPath;

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 保存分片文件到临时目录
     */
    public String saveChunkFile(byte[] chunkBytes, String fileMd5, int chunkIndex) {
        // 创建临时目录（按文件MD5分目录，避免冲突）
        File tempDir = new File(chunkTempPath + File.separator + fileMd5);
        if (!tempDir.exists()) {
            boolean mkdirs = tempDir.mkdirs();
            if (!mkdirs) {
                log.error("创建分片临时目录失败：{}", tempDir.getAbsolutePath());
                return null;
            }
        }

        // 分片文件名格式：{md5}-{index}
        String chunkFileName = fileMd5 + "-" + chunkIndex;
        File chunkFile = new File(tempDir, chunkFileName);
        try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
            fos.write(chunkBytes);
            log.info("分片{}保存成功，路径：{}", chunkIndex, chunkFile.getAbsolutePath());
            return chunkFile.getAbsolutePath();
        } catch (IOException e) {
            log.error("保存分片{}失败", chunkIndex, e);
            return null;
        }
    }

    // 修正mergeChunkFiles方法的目标文件路径
    public String mergeChunkFiles(String fileMd5, String fileName, String fileSuffix) {
        File tempDir = new File(chunkTempPath + File.separator + fileMd5);
        if (!tempDir.exists() || tempDir.listFiles() == null) {
            log.error("分片目录不存在：{}", tempDir.getAbsolutePath());
            return null;
        }

        // 按分片索引排序
        File[] chunkFiles = tempDir.listFiles((dir, name) -> name.startsWith(fileMd5 + "-"));
        if (chunkFiles == null || chunkFiles.length == 0) {
            log.error("分片文件为空：{}", tempDir.getAbsolutePath());
            return null;
        }
        Arrays.sort(chunkFiles, Comparator.comparingInt(f -> Integer.parseInt(f.getName().split("-")[1])));

        // 目标文件路径：必须使用配置的upload.path
        String targetFileName = fileName + "." + fileSuffix;
        File targetFile = new File(uploadPath, targetFileName); // 这里的uploadPath是配置的./upload

        try (FileChannel targetChannel = new FileOutputStream(targetFile).getChannel()) {
            for (File chunkFile : chunkFiles) {
                try (FileChannel chunkChannel = new FileInputStream(chunkFile).getChannel()) {
                    targetChannel.transferFrom(chunkChannel, targetChannel.size(), chunkChannel.size());
                }
                chunkFile.delete(); // 删除已合并的分片
            }
            tempDir.delete(); // 删除临时目录
            log.info("文件合并完成，保存路径：{}", targetFile.getAbsolutePath());
            return targetFile.getAbsolutePath();
        } catch (IOException e) {
            log.error("合并分片失败", e);
            return null;
        }
    }
    /**
     * 计算文件MD5（前端/后端统一校验，避免分片错乱）
     */
    public String calculateFileMd5(byte[] fileBytes) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(fileBytes);
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("计算文件MD5失败", e);
            return null;
        }
    }
}