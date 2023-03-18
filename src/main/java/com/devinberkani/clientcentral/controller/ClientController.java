package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/client")
public class ClientController {
    ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
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
        clientService.saveNewClient(client);
        return "redirect:/admin/dashboard?create";
    }

    // handle view edit client page
    @GetMapping("/edit/{clientId}")
    public String getEditClient(@PathVariable("clientId") Long clientId,
                                Model model) {
        ClientDto client = clientService.findClientById(clientId);
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
        return "redirect:/admin/dashboard?update";
    }

    // handle delete client
    @GetMapping("/delete/{clientId}")
    public String getDeleteClient(@PathVariable("clientId") Long clientId) {
        clientService.deleteClientById(clientId);
        return "redirect:/admin/dashboard?delete";
    }

    // handle view client
    @GetMapping("/view/{clientId}")
    public String getViewClient(@PathVariable("clientId") Long clientId,
                                Model model) {
        ClientDto client = clientService.findClientById(clientId);
        model.addAttribute("client", client);
        return "admin/view_client";
    }

}
