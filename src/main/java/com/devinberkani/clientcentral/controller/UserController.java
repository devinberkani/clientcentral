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
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
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
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", null, "There is already a user with this email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user",user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success"; // pass success parameter
    }

}
