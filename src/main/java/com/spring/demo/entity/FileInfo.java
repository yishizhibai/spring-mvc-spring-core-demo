package com.spring.demo.entity;

import lombok.Data;
import java.util.Date;

@Data
public class FileInfo {
    private String fileId;
    private String fileName;
    private String fileSuffix;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private String userId;
    private Date uploadTime;
    private byte[] fileData; // 新增：对应数据库的file_data字段（BLOB）
}