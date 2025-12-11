package com.spring.demo.service.file.impl;

import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.service.file.FileService;
import com.spring.demo.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 文件服务实现类
 * 修复：解决FileUtils注入、路径初始化、重复代码、空指针等问题
 */
@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    // 内存存储文件信息（线程安全）
    private final Map<String, FileInfo> fileMap = new ConcurrentHashMap<>();

    // FileUtils通过构造函数注入（解决未初始化问题）
    private final FileUtils fileUtils;

    // 上传路径（从配置文件注入，修正注解语法）
    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 构造函数：注入FileUtils
     * 解决“变量fileUtils可能尚未初始化”和“未分配”问题
     */
    public FileServiceImpl(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    /**
     * Bean初始化后执行：创建上传目录
     * 解决@Value注入时机晚于构造函数的问题
     */
    @PostConstruct
    public void initUploadDir() {
        // 兜底处理：配置缺失时使用项目根目录
        if (uploadPath == null || uploadPath.trim().isEmpty()) {
            uploadPath = System.getProperty("user.dir") + "/upload";
            log.warn("文件上传路径配置缺失，使用默认路径：{}", uploadPath);
        }
        // 确保目录存在（调用FileUtils的创建方法）
        fileUtils.createDirIfNotExists(uploadPath);
        log.info("文件上传目录初始化完成：{}", uploadPath);
    }

    /**
     * 普通文件上传
     */
    @Override
    public ResultVO upload(MultipartFile file) {
        // 校验文件是否为空
        if (file.isEmpty()) {
            log.error("文件上传失败：文件为空");
            return ResultVO.error("文件不能为空");
        }

        try {
            // 解析文件基本信息
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.lastIndexOf(".") == -1) {
                return ResultVO.error("文件名解析失败或文件无后缀");
            }
            String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            String fileSuffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            String filePath = uploadPath + File.separator + originalFileName;

            // 保存文件到服务器
            File targetFile = new File(filePath);
            file.transferTo(targetFile);
            log.info("普通文件上传成功：{}，路径：{}", originalFileName, filePath);

            // 构建文件信息并存储（提取重复逻辑，解决代码重复问题）
            FileInfo fileInfo = buildFileInfo(fileName, fileSuffix, filePath, file.getSize());
            fileMap.put(fileInfo.getFileId(), fileInfo);

            return ResultVO.success("文件上传成功", fileInfo);
        } catch (IOException e) {
            log.error("普通文件上传失败", e);
            return ResultVO.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 保存分片文件信息（分片上传后调用）
     */
    @Override
    public ResultVO saveFileInfo(String fileName, String fileSuffix, String filePath) {
        try {
            File targetFile = new File(filePath);
            if (!targetFile.exists()) {
                log.error("保存文件信息失败：文件不存在，路径：{}", filePath);
                return ResultVO.error("文件不存在");
            }

            // 构建文件信息并存储（复用逻辑，解决代码重复问题）
            FileInfo fileInfo = buildFileInfo(fileName, fileSuffix, filePath, targetFile.length());
            fileMap.put(fileInfo.getFileId(), fileInfo);

            log.info("分片文件信息保存成功：{}，路径：{}", fileName + "." + fileSuffix, filePath);
            return ResultVO.success("文件上传成功", fileInfo);
        } catch (Exception e) {
            log.error("保存分片文件信息失败", e);
            return ResultVO.error("保存文件信息失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有文件
     */
    @Override
    public List<FileInfo> listAllFiles() {
        return new ArrayList<>(fileMap.values());
    }

    /**
     * 按文件名模糊查询
     */
    @Override
    public List<FileInfo> searchFileByName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return listAllFiles();
        }
        return fileMap.values().stream()
                .filter(fileInfo -> fileInfo.getFileName().contains(fileName.trim()))
                .collect(Collectors.toList());
    }

    /**
     * 按文件ID查询
     */
    @Override
    public FileInfo getFileById(String fileId) {
        return fileMap.get(fileId);
    }

    /**
     * 提取重复逻辑：构建FileInfo对象
     * 解决“重复的代码段”提示
     */
    private FileInfo buildFileInfo(String fileName, String fileSuffix, String filePath, long fileSize) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(UUID.randomUUID().toString());
        fileInfo.setFileName(fileName);
        fileInfo.setFileSuffix(fileSuffix);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileType(FileUtils.getFileType(fileSuffix));
        fileInfo.setFilePath(filePath);
        fileInfo.setUploadTime(LocalDateTime.now());
        // 可选：格式化文件大小（前端可直接使用）
        fileInfo.setFormattedFileSize(FileUtils.formatFileSize(fileSize));
        return fileInfo;
    }
}