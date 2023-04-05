package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.service.ClientService;
import com.devinberkani.clientcentral.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/client")
public class ClientController {
    ClientService clientService;
    NoteService noteService;

    public ClientController(ClientService clientService, NoteService noteService) {
        this.clientService = clientService;
        this.noteService = noteService;
    }

    // handle view create new client page
    @GetMapping("/create")
    public String getCreateClient(Model model) {
        ClientDto client = new ClientDto();
        // for birthday datepicker
        LocalDate currentYear = LocalDate.now();
        LocalDate nextYear = LocalDate.now().plusYears(1).minusDays(1);
        model.addAttribute("client", client);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("nextYear", nextYear);
        return "admin/create_client";
    }

    // handle submit create new client page
    @PostMapping("/create")
    public String postCreateClient(@Valid @ModelAttribute("client") ClientDto client,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) { // check if form has errors
            model.addAttribute("client", client);
            return "admin/create_client";
        }
        // return client id to append to create param for client view link
        Long clientId = clientService.saveNewClient(client);
        return "redirect:/admin/dashboard?create=" + clientId;
    }

    // handle view edit client page
    @GetMapping("/edit/{clientId}")
    public String getEditClient(@PathVariable("clientId") Long clientId,
                                Model model) {
        ClientDto client = clientService.findClientById(clientId);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // for birthday datepicker
        LocalDate currentYear = LocalDate.now();
        LocalDate nextYear = LocalDate.now().plusYears(1).minusDays(1);
        model.addAttribute("client", client);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("nextYear", nextYear);
        return "admin/edit_client";
    }

    // handle submit edit client
    @PostMapping("/edit/{clientId}")
    public String postEditClient(@PathVariable("clientId") Long clientId,
                                 @Valid @ModelAttribute("client") ClientDto client,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) { // check if form has errors
            model.addAttribute("client", client);
            return "admin/edit_client";
        }
        clientService.updateClient(client, clientId);
        return "redirect:/admin/dashboard?update=" + clientId;
    }

    // handle delete client
    @GetMapping("/delete/{clientId}")
    public String getDeleteClient(@PathVariable("clientId") Long clientId) {
        int deleted = clientService.deleteClientById(clientId);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/admin/dashboard?delete";
    }

    // handle view client
    @GetMapping("/{clientId}")
    public String getViewClient(@PathVariable("clientId") Long clientId,
                                Model model) {
        ClientDto client = clientService.findClientById(clientId);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("client", client);
        Page<NoteDto> page = noteService.getNotesPage(client, 1, "desc");
        return getPage(page, 1, "desc", model);
    }

    // handle view client with search parameters
    @GetMapping("/{clientId}/search")
    public String getSearchViewClient(@PathVariable("clientId") Long clientId,
                                      @RequestParam("p") int pageNo,
                                      @RequestParam("d") String sortDir,
                                      Model model) {
        ClientDto client = clientService.findClientById(clientId);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("client", client);
        Page<NoteDto> page = noteService.getNotesPage(client, pageNo, sortDir);
        return getPage(page, pageNo, sortDir, model);
    }

    // getPage method to reduce repetitive code and return correct view client page
    private String getPage(Page<NoteDto> page,
                           int pageNo,
                           String sortDir,
                           Model model) {
        List<NoteDto> notes = page.getContent();
        model.addAttribute("notes", notes);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");
        return "admin/view_client";
    }

}
