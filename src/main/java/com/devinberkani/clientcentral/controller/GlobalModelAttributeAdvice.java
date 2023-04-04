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
    public void addLoggedInUser(Model model) {

        User currentUser = userService.getCurrentUser();

        // Check if the current user is not null, add to model if not
        if (currentUser != null) {
            model.addAttribute("currentUserFirstName", currentUser.getFirstName());
            model.addAttribute("currentUserLastName", currentUser.getLastName());
        }
    }


}
