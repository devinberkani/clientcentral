package com.devinberkani.clientcentral.controller;

import com.devinberkani.clientcentral.dto.UserDto;
import com.devinberkani.clientcentral.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // user login
    @GetMapping("/login")
    public String userGetLogin() {
        return "login";
    }

    // user view registration form
    @GetMapping("/register")
    public String userGetRegister(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // user submit registration form
    @PostMapping("/register/save")
    public String userPostRegister(@Valid @ModelAttribute("user") UserDto user, BindingResult result, Model model) {
        if (userService.existsByEmail(user.getEmail())) { // check if user already exists
            result.rejectValue("email", null, "There is already a user with this email");
        }
        if (result.hasErrors()) { // check if form has errors
            model.addAttribute("user",user);
            return "register";
        }
        userService.saveNewUser(user);
        return "redirect:/register?success"; // pass success parameter
    }

}
