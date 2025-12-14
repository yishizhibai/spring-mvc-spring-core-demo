package com.spring.demo.controller;

import com.spring.demo.aop.annotation.RateLimit;
import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.service.file.FileService;
import com.spring.demo.util.FileChunkUtils;
import com.spring.demo.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;
    private final FileUtils fileUtils;
    private final FileChunkUtils fileChunkUtils;

    @Value("${file.upload.chunk.size}")
    private long chunkSize;

    @Autowired
    public FileController(FileService fileService, FileUtils fileUtils, FileChunkUtils fileChunkUtils) {
        this.fileService = fileService;
        this.fileUtils = fileUtils;
        this.fileChunkUtils = fileChunkUtils;
    }

    @GetMapping("/index")
    public String index(Model model, @RequestParam(required = false) String fileName) {
        List<FileInfo> fileList = fileName != null ? fileService.searchFileByName(fileName) : fileService.listAllFiles();
        fileList.forEach(f -> f.setFormattedFileSize(FileUtils.formatFileSize(f.getFileSize())));
        model.addAttribute("fileList", fileList);
        model.addAttribute("searchName", fileName == null ? "" : fileName);
        return "index";
    }

    @PostMapping("/upload")
    @ResponseBody
    @RateLimit(qps = 5, message = "上传频繁，请稍后再试")
    public ResultVO upload(@RequestParam("file") MultipartFile file) {
        return fileService.upload(file);
    }

    @PostMapping("/upload/chunk")
    @ResponseBody
    @RateLimit(qps = 10, message = "分片上传频繁，请稍后再试")
    public ResultVO uploadChunk(
            @RequestParam("file") MultipartFile chunkFile,
            @RequestParam("fileMd5") String fileMd5,
            @RequestParam("chunkIndex") int chunkIndex,
            @RequestParam("totalChunks") int totalChunks
    ) {
        try {
            String chunkPath = fileChunkUtils.saveChunkFile(chunkFile.getBytes(), fileMd5, chunkIndex);
            if (chunkPath == null) return ResultVO.error("分片" + chunkIndex + "上传失败");
            return ResultVO.success("分片" + chunkIndex + "上传成功", new ChunkUploadResult(chunkIndex, totalChunks, fileMd5));
        } catch (IOException e) {
            return ResultVO.error("分片上传失败：" + e.getMessage());
        }
    }

    @PostMapping("/upload/merge")
    @ResponseBody
    // 可根据需要添加限流注解
    // @RateLimit(qps = 5, message = "文件合并频繁，请稍后再试")
    public ResultVO mergeChunks(
            @RequestParam("fileMd5") String fileMd5,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileSuffix") String fileSuffix
    ) {
        String targetPath = fileChunkUtils.mergeChunkFiles(fileMd5, fileName, fileSuffix);
        if (targetPath == null) return ResultVO.error("合并失败");
        return fileService.saveFileInfo(fileName, fileSuffix, targetPath);
    }

    @GetMapping("/chunk/size")
    @ResponseBody
    // 可根据需要添加限流注解
    // @RateLimit(qps = 20, message = "获取分片大小频繁，请稍后再试")
    public ResultVO getChunkSize() {
        return ResultVO.success("获取分片大小成功", chunkSize);
    }

    @GetMapping("/download/{fileId}")
    // 可根据需要添加限流注解
    // @RateLimit(qps = 5, message = "文件下载频繁，请稍后再试")
    public ResponseEntity<byte[]> download(@PathVariable String fileId) {
        FileInfo fileInfo = fileService.getFileById(fileId);
        if (fileInfo == null) return ResponseEntity.notFound().build();

        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(fileInfo.getFilePath()));
            HttpHeaders headers = new HttpHeaders();
            String downloadName = fileInfo.getFileName() + "." + fileInfo.getFileSuffix();
            headers.setContentDispositionFormData("attachment",
                    new String(downloadName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list")
    @ResponseBody
    // 可根据需要添加限流注解
    // @RateLimit(qps = 10, message = "文件列表查询频繁，请稍后再试")
    public ResultVO listFiles(@RequestParam(required = false) String fileName) {
        List<FileInfo> fileList = fileName != null ? fileService.searchFileByName(fileName) : fileService.listAllFiles();
        fileList.forEach(f -> f.setFormattedFileSize(FileUtils.formatFileSize(f.getFileSize())));
        return ResultVO.success("查询成功", fileList);
    }

    // 内部类：分片上传结果
    public static class ChunkUploadResult {
        private int chunkIndex;
        private int totalChunks;
        private String fileMd5;

        public ChunkUploadResult(int chunkIndex, int totalChunks, String fileMd5) {
            this.chunkIndex = chunkIndex;
            this.totalChunks = totalChunks;
            this.fileMd5 = fileMd5;
        }

        // getter/setter 方法（必须添加，否则前端无法获取数据）
        public int getChunkIndex() {
            return chunkIndex;
        }

        public void setChunkIndex(int chunkIndex) {
            this.chunkIndex = chunkIndex;
        }

        public int getTotalChunks() {
            return totalChunks;
        }

        public void setTotalChunks(int totalChunks) {
            this.totalChunks = totalChunks;
        }

        public String getFileMd5() {
            return fileMd5;
        }

        public void setFileMd5(String fileMd5) {
            this.fileMd5 = fileMd5;
        }
    }
}