package com.devinberkani.clientcentral.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/reminders")
public class RemindersController {

    @GetMapping
    public String getReminders() {
        return "admin/reminders";
    }

}
