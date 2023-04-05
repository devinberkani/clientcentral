package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/reminders")
public class RemindersController {

    ClientService clientService;

    public RemindersController(ClientService clientService) {
        this.clientService = clientService;
    }

    // handle view reminders page
    @GetMapping
    public String getReminders(Model model) {
        return getPage(1, 1, "asc", model);
    }

    @GetMapping("/search")
    public String getSearchReminders(@RequestParam("p1") int todayPageNo,
                                     @RequestParam("p2") int upcomingPageNo,
                                     @RequestParam("d") String sortDir,
                                     Model model) {
        return getPage(todayPageNo, upcomingPageNo, sortDir, model);
    }

    // getPage method to reduce repetitive code and return correct version of reminders
    public String getPage(int todayPageNo,
                          int upcomingPageNo,
                          String sortDir,
                          Model model) {
        Page<ClientDto> birthdaysTodayPage = clientService.getTodayBirthdays(todayPageNo);
        Page<ClientDto> upcomingBirthdaysPage = clientService.getUpcomingBirthdays(upcomingPageNo, sortDir);
        List<ClientDto> birthdaysToday = birthdaysTodayPage.getContent();
        List<ClientDto> upcomingBirthdays = upcomingBirthdaysPage.getContent();
        model.addAttribute("birthdaysToday", birthdaysToday);
        model.addAttribute("upcomingBirthdays", upcomingBirthdays);
        model.addAttribute("todayPageNo", todayPageNo);
        model.addAttribute("upcomingPageNo", upcomingPageNo);
        model.addAttribute("totalTodayBirthdayPages", birthdaysTodayPage.getTotalPages());
        model.addAttribute("totalUpcomingBirthdayPages", upcomingBirthdaysPage.getTotalPages());
        model.addAttribute("totalTodayBirthdayItems", birthdaysTodayPage.getTotalElements());
        model.addAttribute("totalUpcomingBirthdayItems", upcomingBirthdaysPage.getTotalElements());
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");
        return "admin/reminders";
    }

}
