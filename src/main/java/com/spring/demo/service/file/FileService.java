package com.spring.demo.service.file;

import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    /**
     * 上传文件
     */
    ResultVO upload(MultipartFile file);

    /**
     * 查询所有文件
     */
    List<FileInfo> listAllFiles();

    /**
     * 根据文件名搜索文件
     */
    List<FileInfo> searchFileByName(String fileName);

    /**
     * 根据文件ID查询文件信息
     */
    FileInfo getFileById(String fileId);
}