package com.spring.demo.service.file.impl;

import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.mapper.FileInfoMapper;
import com.spring.demo.service.file.FileService;
import com.spring.demo.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return ResultVO.error("文件名不能为空");
            }

            // 拆分文件名和后缀
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));

            // 封装文件信息（包含二进制数据）
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(UUID.randomUUID().toString());
            fileInfo.setFileName(fileName);
            fileInfo.setFileSuffix(fileSuffix);
            fileInfo.setFileType(FileUtils.getFileType(fileSuffix));
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFilePath("数据库存储"); // 标记数据存在数据库
            fileInfo.setUserId("default_user_id");
            fileInfo.setUploadTime(new Date());
            fileInfo.setFileData(file.getBytes()); // 核心：获取文件二进制数据并赋值

            // 插入数据库（包含二进制数据）
            int rows = fileInfoMapper.insertFileInfo(fileInfo);
            return rows > 0 ? ResultVO.success("文件保存到数据库成功", fileInfo) : ResultVO.error("保存文件失败");
        } catch (Exception e) {
            return ResultVO.error("文件上传失败：" + e.getMessage());
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