package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.service.ClientService;
import com.devinberkani.clientcentral.service.FileService;
import com.devinberkani.clientcentral.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Controller
@RequestMapping("/admin/notes")
public class NoteController {

    ClientService clientService;
    NoteService noteService;
    FileService fileService;

    public NoteController(ClientService clientService, NoteService noteService, FileService fileService) {
        this.clientService = clientService;
        this.noteService = noteService;
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

    // handle post create new note

    @PostMapping("/client/{clientId}/create")
    public String postCreateNote(@Valid @ModelAttribute("note") NoteDto note,
                                 BindingResult result,
                                 @PathVariable("clientId") Long clientId,
                                 @RequestParam("multipartFiles") MultipartFile[] multipartFiles) throws IOException {
        if (result.hasErrors()) {
            return "admin/create_note";
        }
        Long noteId = noteService.saveNewNote(note, clientId);
        for (MultipartFile multipartFile : multipartFiles) {
            // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set filepath for any existing files - should get current logged in user
            if (!multipartFile.isEmpty()) {
                fileService.saveNewFile(multipartFile, (long) 1, clientId, noteId);
            }
        }
        return "redirect:/admin/client/{clientId}?create";
    }

    // handle delete note

    @GetMapping("/delete/client/{clientId}/note/{noteId}")
    public String deleteNote(@PathVariable("noteId") Long noteId,
                             @PathVariable("clientId") Long clientId,
                             Model model) {
        noteService.deleteNote(noteId, clientId);
        model.addAttribute(clientId);
        return "redirect:/admin/client/{clientId}?delete";
    }

}
