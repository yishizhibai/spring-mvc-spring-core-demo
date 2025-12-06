package com.spring.demo.repository;

import com.spring.demo.entity.FileInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FileRepository {
    // 内存存储文件元信息
    private final List<FileInfo> fileList = new ArrayList<>();

    /**
     * 保存文件信息
     */
    public void save(FileInfo fileInfo) {
        fileList.add(fileInfo);
    }

    /**
     * 查询所有文件
     */
    public List<FileInfo> findAll() {
        return new ArrayList<>(fileList); // 返回副本，避免外部修改
    }

    /**
     * 根据文件ID查询
     */
    public Optional<FileInfo> findById(String fileId) {
        return fileList.stream().filter(f -> f.getFileId().equals(fileId)).findFirst();
    }

    /**
     * 根据纯文件名搜索（精准匹配）
     */
    public List<FileInfo> findByFileName(String fileName) {
        List<FileInfo> result = new ArrayList<>();
        for (FileInfo fileInfo : fileList) {
            if (fileInfo.getFileName().equals(fileName)) {
                result.add(fileInfo);
            }
        }
        return result;
    }
}