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

    // handle redirect to dashboard
    @GetMapping("/")
    public String redirectToLogin() {
        return "login";
    }

    // handle view user login
    @GetMapping("/login")
    public String getUserLogin() {
        return "login";
    }

    // handle view user registration form
    @GetMapping("/register")
    public String getUserRegister(Model model) {
        UserDto newUser = new UserDto();
        model.addAttribute("newUser", newUser);
        return "register";
    }

    // handle submit user registration form
    @PostMapping("/register/save")
    public String postUserRegister(@Valid @ModelAttribute("newUser") UserDto newUser,
                                   BindingResult result,
                                   Model model) {
        if (userService.existsByEmail(newUser.getEmail())) { // check if user already exists
            result.rejectValue("email", null, "There is already a user with this email");
        }
        if (result.hasErrors()) { // check if form has errors
            model.addAttribute("user",newUser);
            return "register";
        }
        userService.saveNewUser(newUser);
        return "redirect:/register?success"; // pass success parameter
    }

}
