package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/dashboard/search")
    public String searchDashboard(@RequestParam("q") String query,
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
