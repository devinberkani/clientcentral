package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.service.ClientService;
import com.devinberkani.clientcentral.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Controller
@RequestMapping("/admin/notes")
public class NoteController {

    ClientService clientService;
    FileService fileService;

    public NoteController(ClientService clientService, FileService fileService) {
        this.clientService = clientService;
        this.fileService = fileService;
    }

    // handle view create new note page
    @GetMapping("/client/{clientId}/create")
    public String getCreateNote(@PathVariable("clientId") Long clientId,
                                  Model model) {
        NoteDto note = new NoteDto();
        model.addAttribute("note", note);
        model.addAttribute("clientId", clientId);
        return "admin/create_note";
    }

    @PostMapping("/client/{clientId}/create")
    public String postCreateNote(@PathVariable("clientId") Long clientId,
                                 @RequestParam("files") MultipartFile[] multipartFiles) {
        for (MultipartFile file: multipartFiles) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        }
        return "redirect:/admin/client/{clientId}";
    }

}
