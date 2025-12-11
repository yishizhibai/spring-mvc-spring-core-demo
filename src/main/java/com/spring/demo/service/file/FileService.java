package com.spring.demo.service.file;

import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.util.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface FileService {
    // 将fileMap定义在接口中（静态变量，所有实现类共享）
    Map<String, FileInfo> fileMap = new ConcurrentHashMap<>();

    // 原有方法...
    ResultVO upload(MultipartFile file);
    List<FileInfo> listAllFiles();
    List<FileInfo> searchFileByName(String fileName);
    FileInfo getFileById(String fileId);

    // 保存文件信息（使用接口的fileMap）
    default ResultVO saveFileInfo(String fileName, String fileSuffix, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return ResultVO.error("文件不存在");
            }

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(UUID.randomUUID().toString());
            fileInfo.setFileName(fileName);
            fileInfo.setFileSuffix(fileSuffix);
            fileInfo.setFileSize(file.length());
            fileInfo.setFileType(FileUtils.getFileType(fileSuffix)); // 需确保getFileType是static
            fileInfo.setFilePath(filePath);
            fileInfo.setUploadTime(LocalDateTime.now());

            // 存入接口的fileMap（不再依赖实现类）
            fileMap.put(fileInfo.getFileId(), fileInfo);
            return ResultVO.success("文件上传成功", fileInfo);
        } catch (Exception e) {
            return ResultVO.error("保存文件信息失败：" + e.getMessage());
        }
    }
}