package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    UserService userService;

    public GlobalModelAttributeAdvice(UserService userService) {
        this.userService = userService;
    }

    // handle making the user attribute accessible to all controllers
    @ModelAttribute
    public void addUserFirstName(Model model) {
        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to get logged in user to pass to global model - should get current logged in user
        User user = userService.findUserById((long) 1);
        model.addAttribute("user", user);
    }

}
