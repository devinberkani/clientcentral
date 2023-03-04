package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.service.FileService;
import com.devinberkani.clientcentral.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

@Controller
@RequestMapping("/admin/upload")
public class FileController {

    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String getUploadCsv() {
        return "admin/upload_form";
    }

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (FileUtil.hasCSVFormat(file)) {
            try {
                fileService.save(file);
                return "redirect:/admin/dashboard?uploadSuccess";
            } catch (Exception e) {
                System.out.println("hi");
                return "redirect:/admin/upload?uploadError";
            }
        }
        System.out.println("hi");
        return "redirect:/admin/upload?formatError";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws FileNotFoundException {
        Resource resource = fileService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
