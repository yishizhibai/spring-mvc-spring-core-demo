package com.spring.demo.controller;

import com.spring.demo.entity.FileInfo;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.service.file.FileService;
import com.spring.demo.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;
    private final FileUtils fileUtils;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Autowired
    public FileController(FileService fileService, FileUtils fileUtils) {
        this.fileService = fileService;
        this.fileUtils = fileUtils;
    }

    @GetMapping("/index")
    public String index(Model model, @RequestParam(required = false) String fileName) {
        // 查询文件列表（支持搜索）
        List<FileInfo> fileList;
        if (fileName != null && !fileName.trim().isEmpty()) {
            fileList = fileService.searchFileByName(fileName.trim());
        } else {
            fileList = fileService.listAllFiles();
        }

        // 修正：格式化文件大小并赋值给新增字段
        for (FileInfo fileInfo : fileList) {
            String formattedSize = fileUtils.formatFileSize(fileInfo.getFileSize());
            fileInfo.setFormattedFileSize(formattedSize); // 赋值给字符串字段
        }

        // 传递数据到前端
        model.addAttribute("fileList", fileList);
        model.addAttribute("searchName", fileName == null ? "" : fileName);
        model.addAttribute("totalCount", fileList.size());
        return "index"; // 对应templates/index.html
    }

    // 其他方法（upload/download/toIndex）保持不变...
    @PostMapping("/upload")
    @ResponseBody
    public ResultVO upload(@RequestParam("file") MultipartFile file) {
        return fileService.upload(file);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable String fileId) {
        // 查询文件信息
        FileInfo fileInfo = fileService.getFileById(fileId);
        if (fileInfo == null) {
            log.error("下载文件失败：ID{}不存在", fileId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            // 读取文件字节
            byte[] fileBytes = Files.readAllBytes(Paths.get(fileInfo.getFilePath()));

            // 构建响应头（设置下载文件名）
            HttpHeaders headers = new HttpHeaders();
            String downloadFileName = fileInfo.getFileName() + "." + fileInfo.getFileSuffix();
            headers.setContentDispositionFormData("attachment", new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("下载文件失败：ID{}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/")
    public String toIndex() {
        return "redirect:/file/index";
    }
}