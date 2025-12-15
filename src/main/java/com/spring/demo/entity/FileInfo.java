package com.spring.demo.entity;

import lombok.Data;
import java.util.Date;

/**
 * 文件信息实体类，对应数据库file_info表
 */
@Data
public class FileInfo {
    // 文件唯一ID（主键）
    private String fileId;
    // 文件名
    private String fileName;
    // 文件后缀（如png、txt）
    private String fileSuffix;
    // 文件类型（如图片、文档）
    private String fileType;
    // 文件大小（字节）
    private Long fileSize;
    // 文件路径（标记为数据库存储）
    private String filePath;
    // 上传用户ID
    private String userId;
    // 上传时间
    private Date uploadTime;
    // 可选：文件二进制数据（若需要存储文件内容）
    private byte[] fileData;
}