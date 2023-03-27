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
    public String getDashboard(Model model) {
        Page<ClientDto> page = clientService.findMatchingClients("", 1, "firstName", "asc"); // page number should always be one for landing page
        return getPage(page, "", 1, "firstName", "asc", model);
    }

    // handle search dashboard
    @GetMapping("/dashboard/search")
    public String getSearchDashboard(@RequestParam("q") String query,
                                   @RequestParam("p") int pageNo,
                                   @RequestParam("s") String sortField,
                                   @RequestParam("d") String sortDir,
                                   Model model) {
        Page<ClientDto> page = clientService.findMatchingClients(query, pageNo, sortField, sortDir); // page number should always be one for landing page
        return getPage(page, query, pageNo, sortField, sortDir, model);
    }

    // getPage method to reduce repetitive code and return correct version of dashboard
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
