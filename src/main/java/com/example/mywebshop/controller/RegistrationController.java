package com.example.mywebshop.controller;

import com.example.mywebshop.config.validation.ValidUser;
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
public class RegistrationController {

    private final IUserService userService;

    @Autowired
    public RegistrationController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/reg")
    public String register(ValidUser validUser) {
        return "reg-form";
    }

    @PostMapping("/reg")
    public String preformRegister(Model ui,
                                  @Valid @ModelAttribute ValidUser validUser,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ui.addAttribute("bindingResult", bindingResult);
            ui.addAttribute("success", false);
        } else {
            userService.registerUser(validUser);
            ui.addAttribute("success", true);
        }
        return "reg-form";
    }
}
