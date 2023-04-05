package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.service.ClientService;
import com.devinberkani.clientcentral.service.FileService;
import com.devinberkani.clientcentral.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Controller
// stores the specified model attributes in the HTTP session to keep it available across multiple HTTP requests
@SessionAttributes("note")
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
        // note needs to be created before creating the path so that id is available
        Long noteId = noteService.saveNewNote(note, clientId);

        // add new files to filesystem
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                fileService.saveNewFile(multipartFile, clientId, noteId);
            }
        }
        return "redirect:/admin/client/{clientId}?create";
    }

    // handle view edit note
    @GetMapping("/edit/client/{clientId}/note/{noteId}")
    public String getEditNote(@PathVariable("clientId") Long clientId,
                              @PathVariable("noteId") Long noteId,
                              Model model) {
        NoteDto note = noteService.findNoteById(noteId);
        if (note == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found");
        }
        model.addAttribute("note", note);
        model.addAttribute("clientId", clientId);
        return "admin/edit_note";
    }

    // handle submit edit note
    @PostMapping("/edit/client/{clientId}/note/{noteId}")
    public String postEditNote(@Valid @ModelAttribute("note") NoteDto note,
                               BindingResult result,
                               @PathVariable("clientId") Long clientId,
                               @PathVariable("noteId") Long noteId,
                               Model model,
                               @RequestParam("multipartFiles") MultipartFile[] multipartFiles) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("note", note);
            return "admin/edit_note";
        }

        // add updated files to filesystem
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                fileService.saveNewFile(multipartFile, clientId, noteId);
            }
        }
        model.addAttribute("clientId", clientId);
        noteService.updateNote(note, noteId, clientId);
        return "redirect:/admin/client/{clientId}?update";
    }

    // handle delete note
    @GetMapping("/delete/client/{clientId}/note/{noteId}")
    public String getDeleteNote(@PathVariable("clientId") Long clientId,
                                @PathVariable("noteId") Long noteId,
                                Model model) {
        int deleted = noteService.deleteNote(noteId, clientId);
        if (deleted == 0) {
            model.addAttribute("errorMessage", "Note not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute(clientId);
        return "redirect:/admin/client/{clientId}?delete";
    }

    // handle delete file from edit note view
    @GetMapping("/edit/client/{clientId}/note/{noteId}/delete/file/{fileId}/{fileReference}")
    public String getDeleteFile(@PathVariable("clientId") Long clientId,
                                @PathVariable("noteId") Long noteId,
                                @PathVariable("fileId") Long fileId,
                                @PathVariable("fileReference") String fileReference) {
        int deleted = fileService.deleteFile(fileId, noteId, clientId, fileReference);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
        return "redirect:/admin/notes/edit/client/{clientId}/note/{noteId}?delete";
    }

}
