package com.spring.demo.mapper;

import com.spring.demo.entity.FileInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileInfoMapper {
    // 插入语句（保持不变）
    @Insert("INSERT INTO file_info(id, file_name, file_suffix, file_type, file_size, file_path, user_id, upload_time, file_data) " +
            "VALUES(#{fileId}, #{fileName}, #{fileSuffix}, #{fileType}, #{fileSize}, #{filePath}, #{userId}, #{uploadTime}, #{fileData})")
    int insertFileInfo(FileInfo fileInfo);

    // 修复：查询时将id别名设为fileId
    @Select("SELECT id AS fileId, file_name AS fileName, file_suffix AS fileSuffix, file_type AS fileType, " +
            "file_size AS fileSize, file_path AS filePath, user_id AS userId, upload_time AS uploadTime, file_data AS fileData " +
            "FROM file_info")
    List<FileInfo> selectAllFileInfo();

    // 修复：查询时将id别名设为fileId
    @Select("SELECT id AS fileId, file_name AS fileName, file_suffix AS fileSuffix, file_type AS fileType, " +
            "file_size AS fileSize, file_path AS filePath, user_id AS userId, upload_time AS uploadTime, file_data AS fileData " +
            "FROM file_info WHERE file_name LIKE CONCAT('%', #{fileName}, '%')")
    List<FileInfo> selectFileInfoByName(String fileName);

    // 修复：查询时将id别名设为fileId
    @Select("SELECT id AS fileId, file_name AS fileName, file_suffix AS fileSuffix, file_type AS fileType, " +
            "file_size AS fileSize, file_path AS filePath, user_id AS userId, upload_time AS uploadTime, file_data AS fileData " +
            "FROM file_info WHERE id = #{fileId}")
    FileInfo selectFileInfoById(String fileId);

    // 删除语句（保持不变）
    @Delete("DELETE FROM file_info WHERE id = #{fileId}")
    int deleteFileInfoById(String fileId);
}