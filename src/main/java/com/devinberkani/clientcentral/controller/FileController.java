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
@RequestMapping("/admin/files")
public class FileController {

    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // handle view bulk upload csv form
    @GetMapping("/upload")
    public String getUploadCsv() {
        return "admin/upload_form";
    }

    // handle submit bulk upload csv form
    @PostMapping("/upload")
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
                return "redirect:/admin/files/upload?uploadError";
            }
        }
        // if the file isn't a csv, show format error message
        return "redirect:/admin/upload?formatError";
    }

    // handle download bulk upload csv file template
    @GetMapping("/download/{fileReference}")
    public ResponseEntity<Resource> downloadCsvTemplate(@PathVariable("fileReference") String fileName) throws FileNotFoundException {
        Resource resource = fileService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                // tells the client that the response body should be treated as a file for download, rather than being displayed in the browser
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // handle download user file from specific client and note
    @GetMapping("/{clientId}/{noteId}/{fileReference}")
    public ResponseEntity<Resource> downloadUserFile(@PathVariable("clientId") Long clientId,
                                                     @PathVariable("noteId") Long noteId,
                                                     @PathVariable("fileReference") String fileName) throws FileNotFoundException {
        Resource resource = fileService.loadFileAsResource(clientId, noteId, fileName);
        return ResponseEntity.ok()
                // tells the client that the response body should be treated as a file for download, rather than being displayed in the browser
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
