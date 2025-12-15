package com.spring.demo.mapper;

import com.spring.demo.entity.FileInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileInfoMapper {
    // 新增：插入file_data字段
    @Insert("INSERT INTO file_info(id, file_name, file_suffix, file_type, file_size, file_path, user_id, upload_time, file_data) " +
            "VALUES(#{fileId}, #{fileName}, #{fileSuffix}, #{fileType}, #{fileSize}, #{filePath}, #{userId}, #{uploadTime}, #{fileData})")
    int insertFileInfo(FileInfo fileInfo);

    @Select("SELECT * FROM file_info")
    List<FileInfo> selectAllFileInfo();

    @Select("SELECT * FROM file_info WHERE file_name LIKE CONCAT('%', #{fileName}, '%')")
    List<FileInfo> selectFileInfoByName(String fileName);

    @Select("SELECT * FROM file_info WHERE id = #{fileId}")
    FileInfo selectFileInfoById(String fileId);
}