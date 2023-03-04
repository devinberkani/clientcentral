package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    ClientService clientService;

    public AdminController(ClientService clientService) {
        this.clientService = clientService;
    }

    // handle view dashboard landing page
    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        Page<ClientDto> page = clientService.getClients("", 1, "firstName", "asc"); // page number should always be one for landing page
        return getPage(page, "", 1, "firstName", "asc", model);
    }

    // handle view create new client page
    @GetMapping("/create")
    public String viewCreateClient(Model model) {
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
    @PostMapping("/dashboard")
    public String submitCreateClient(@Valid @ModelAttribute("client") ClientDto client,
                                     BindingResult result,
                                     Model model) {
        if (result.hasErrors()) { // check if form has errors
            model.addAttribute("client", client);
            return "admin/create_client";
        }
        clientService.saveNewClient(client);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard/search")
    public String searchClients(@RequestParam("q") String query,
                                  @RequestParam("p") int pageNo,
                                  @RequestParam("s") String sortField,
                                  @RequestParam("d") String sortDir,
                                  Model model) {
        Page<ClientDto> page = clientService.getClients(query, pageNo, sortField, sortDir); // page number should always be one for landing page
        return getPage(page, query, pageNo, sortField, sortDir, model);
    }

    private String getPage(Page<ClientDto> page,
                           String query,
                           int pageNo,
                           String sortField,
                           String sortDir,
                           Model model) {
        List<ClientDto> clients = page.getContent();
        model.addAttribute("clients", clients);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("query", query);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");
        return "admin/dashboard";
    }
}
