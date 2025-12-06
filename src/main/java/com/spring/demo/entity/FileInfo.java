package com.spring.demo.entity;

import java.time.LocalDateTime;

public class FileInfo {
    // 原有字段不变...
    private String fileId;
    private String fileName;
    private String fileSuffix;
    private Long fileSize; // 原始字节数（Long类型）
    private String fileType;
    private String filePath;
    private LocalDateTime uploadTime;

    // 新增：格式化后的文件大小（如"29B"、"1.2MB"）
    private String formattedFileSize;

    // 原有getter/setter不变...
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFileSuffix() { return fileSuffix; }
    public void setFileSuffix(String fileSuffix) { this.fileSuffix = fileSuffix; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }

    // 新增字段的getter/setter
    public String getFormattedFileSize() { return formattedFileSize; }
    public void setFormattedFileSize(String formattedFileSize) { this.formattedFileSize = formattedFileSize; }
}