package com.spring.demo.service.file.impl;

import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.mapper.FileInfoMapper;
import com.spring.demo.service.file.FileService;
import com.spring.demo.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileInfoMapper fileInfoMapper;

    public FileServiceImpl(FileInfoMapper fileInfoMapper) {
        this.fileInfoMapper = fileInfoMapper;
    }

    @Override
    public ResultVO upload(MultipartFile file) {
        try {
            FileInfo fileInfo = new FileInfo();
            // 关键：生成唯一的fileId（UUID），确保插入数据库的id字段不为null
            fileInfo.setFileId(UUID.randomUUID().toString().replace("-", ""));
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return ResultVO.error("文件名不能为空");
            }
            // 处理文件名和后缀
            String fileName = originalFilename.contains(".")
                    ? originalFilename.substring(0, originalFilename.lastIndexOf("."))
                    : originalFilename;
            String fileSuffix = originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    : "";
            // 赋值其他字段
            fileInfo.setFileName(fileName);
            fileInfo.setFileSuffix(fileSuffix);
            fileInfo.setFileType(getFileType(fileSuffix)); // 需实现文件类型判断方法
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFilePath("database_storage"); // 标记存储方式
            fileInfo.setUserId("default_user"); // 默认用户ID
            fileInfo.setUploadTime(new Date());
            fileInfo.setFileData(file.getBytes()); // 存储文件二进制数据

            // 插入数据库
            int rows = fileInfoMapper.insertFileInfo(fileInfo);
            return rows > 0 ? ResultVO.success("文件上传成功", fileInfo) : ResultVO.error("文件上传失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error("文件上传失败：" + e.getMessage());
        }
    }

    // 辅助方法：根据后缀判断文件类型
    private String getFileType(String suffix) {
        if (suffix == null || suffix.isEmpty()) {
            return "未知类型";
        }
        suffix = suffix.toLowerCase();
        if (Arrays.asList("jpg", "png", "gif", "jpeg").contains(suffix)) {
            return "图片";
        } else if (Arrays.asList("txt", "doc", "docx", "pdf").contains(suffix)) {
            return "文档";
        } else if (Arrays.asList("mp4", "avi", "mov").contains(suffix)) {
            return "视频";
        } else {
            return "其他";
        }
    }

    // 其他方法（listAllFiles、searchFileByName、getFileById）不变
    @Override
    public List<FileInfo> listAllFiles() {
        return fileInfoMapper.selectAllFileInfo();
    }

    @Override
    public List<FileInfo> searchFileByName(String fileName) {
        return fileInfoMapper.selectFileInfoByName(fileName);
    }

    @Override
    public FileInfo getFileById(String fileId) {
        return fileInfoMapper.selectFileInfoById(fileId);
    }
    @Override
    public int deleteFileById(String fileId) {
        return fileInfoMapper.deleteFileInfoById(fileId);
    }

    @Override
    public ResultVO saveFileInfo(String fileName, String fileSuffix, String filePath) {
        // 该方法可保留，用于手动保存信息（无二进制数据）
        try {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(UUID.randomUUID().toString());
            fileInfo.setFileName(fileName);
            fileInfo.setFileSuffix(fileSuffix);
            fileInfo.setFileType(FileUtils.getFileType(fileSuffix));
            fileInfo.setFileSize(0L);
            fileInfo.setFilePath(filePath);
            fileInfo.setUserId("default_user_id");
            fileInfo.setUploadTime(new Date());
            fileInfo.setFileData(null); // 无二进制数据

            int rows = fileInfoMapper.insertFileInfo(fileInfo);
            return rows > 0 ? ResultVO.success("文件信息保存成功", fileInfo) : ResultVO.error("保存文件信息失败");
        } catch (Exception e) {
            return ResultVO.error("保存文件信息失败：" + e.getMessage());
        }
    }
}