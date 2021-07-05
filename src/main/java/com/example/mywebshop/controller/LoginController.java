package com.example.mywebshop.controller;

import com.example.mywebshop.entity.SystemUser;
import com.example.mywebshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginController {

    private final IUserService userService;

    @Autowired
    public LoginController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login-form";
    }

    @GetMapping("/reg")
    public String register(SystemUser systemUser) {
        return "reg-form";
    }

    @PostMapping("/reg")
    public String preformRegister(Model ui,
                                  @Valid @ModelAttribute SystemUser systemUser,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ui.addAttribute("bindingResult", bindingResult);
            ui.addAttribute("success", false);
        } else {
            userService.registerUser(systemUser);
            ui.addAttribute("success", true);
        }
        return "reg-form";
    }
}
