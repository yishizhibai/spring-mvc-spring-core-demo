package com.spring.demo.controller;

import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.service.file.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // 文件上传接口
    @PostMapping("/upload")
    public ResultVO upload(@RequestParam("file") MultipartFile file) {
        return fileService.upload(file);
    }

    // 查询所有文件
    @GetMapping("/list")
    public ResultVO listAllFiles() {
        List<FileInfo> fileList = fileService.listAllFiles();
        return ResultVO.success("查询成功", fileList);
    }

    // 模糊查询文件
    @GetMapping("/search")
    public ResultVO searchFileByName(@RequestParam("fileName") String fileName) {
        List<FileInfo> fileList = fileService.searchFileByName(fileName);
        return ResultVO.success("查询成功", fileList);
    }

    // 根据ID查询文件
    @GetMapping("/{fileId}")
    public ResultVO getFileById(@PathVariable String fileId) {
        FileInfo fileInfo = fileService.getFileById(fileId);
        return fileInfo != null ? ResultVO.success("查询成功", fileInfo) : ResultVO.error("文件不存在");
    }
}