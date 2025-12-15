package com.spring.demo.service.file;

import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    ResultVO upload(MultipartFile file);
    List<FileInfo> listAllFiles();
    List<FileInfo> searchFileByName(String fileName);
    FileInfo getFileById(String fileId);
    ResultVO saveFileInfo(String fileName, String fileSuffix, String filePath);
    int deleteFileById(String fileId);
}