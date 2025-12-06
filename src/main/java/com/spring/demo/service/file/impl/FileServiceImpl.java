package com.spring.demo.service.file.impl;

import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.repository.FileRepository;
import com.spring.demo.service.file.FileService;
import com.spring.demo.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;
    private final FileUtils fileUtils;

    // 构造器注入（推荐）
    @Autowired
    public FileServiceImpl(FileRepository fileRepository, FileUtils fileUtils) {
        this.fileRepository = fileRepository;
        this.fileUtils = fileUtils;
    }

    @Override
    public ResultVO upload(MultipartFile file) {
        // 校验文件是否为空
        if (file.isEmpty()) {
            log.error("文件上传失败：文件为空");
            return ResultVO.error("文件不能为空");
        }

        // 校验文件格式
        if (!fileUtils.checkFileSuffix(file)) {
            log.error("文件上传失败：格式不支持，文件名：{}", file.getOriginalFilename());
            return ResultVO.error("仅支持txt/pdf/docx/png/jpg格式");
        }

        try {
            // 保存文件到本地
            String filePath = fileUtils.saveFile(file);
            String originalFilename = file.getOriginalFilename();

            // 构建文件元信息
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(UUID.randomUUID().toString().replace("-", ""));
            fileInfo.setFileName(fileUtils.getPureFileName(originalFilename));
            fileInfo.setFileSuffix(originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase());
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(fileUtils.getFileType(fileInfo.getFileSuffix()));
            fileInfo.setFilePath(filePath);
            fileInfo.setUploadTime(LocalDateTime.now());

            // 保存文件元信息到内存
            fileRepository.save(fileInfo);
            log.info("文件元信息保存成功：{}", fileInfo.getFileName());
            return ResultVO.success(fileInfo);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ResultVO.error("文件上传失败：" + e.getMessage());
        }
    }

    @Override
    public List<FileInfo> listAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public List<FileInfo> searchFileByName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return listAllFiles();
        }
        return fileRepository.findByFileName(fileName.trim());
    }

    @Override
    public FileInfo getFileById(String fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }
}