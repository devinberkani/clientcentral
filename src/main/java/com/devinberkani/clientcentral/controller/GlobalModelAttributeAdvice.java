package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.service.UserService;
import com.devinberkani.clientcentral.util.SecurityUtils;
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
        // Get the current user from SecurityUtils
        org.springframework.security.core.userdetails.User currentUser = SecurityUtils.getCurrentUser();

        // Check if the current user is not null and has a non-null username
        if (currentUser != null && currentUser.getUsername() != null) {
            User user = userService.findUserByEmail(currentUser.getUsername());

            // Check if a user is found by the email
            if (user != null) {
                model.addAttribute("user", user);
            }
        }
    }


}
