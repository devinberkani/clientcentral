package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.service.FileService;
import com.devinberkani.clientcentral.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;

@Controller
@RequestMapping("/admin/upload")
public class FileController {

    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    // handle view bulk upload csv form
    @GetMapping
    public String getUploadCsv() {
        return "admin/upload_form";
    }

    // handle submit bulk upload csv form
    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        if (FileUtil.hasCSVFormat(file)) {
            try {
                fileService.saveCsv(file);
                return "redirect:/admin/dashboard?uploadSuccess";
            } catch (RuntimeException e) {
                // if the file was a csv and there was an error, have user check their csv formatting based on instructions and try again
                System.out.println(e.getMessage());
                String errorMessage = e.getMessage();
                redirectAttributes.addAttribute("errorMessage", errorMessage);
                return "redirect:/admin/upload?uploadError";
            }
        }
        // if the file isn't a csv, show format error message
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
