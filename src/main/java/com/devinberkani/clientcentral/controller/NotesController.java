package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/notes")
public class NotesController {

    ClientService clientService;

    public NotesController(ClientService clientService) {
        this.clientService = clientService;
    }

    // handle view create new note page
    @GetMapping("/client/{clientId}/create")
    public String getCreateNote(@PathVariable("clientId") Long clientId,
                                  Model model) {
        model.addAttribute("clientId", clientId);
        return "admin/create_note";
    }

}
