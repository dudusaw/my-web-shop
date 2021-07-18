package com.example.mywebshop.controller;

import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class SharedModelsController {

    private final IUserService userService;

    @Autowired
    public SharedModelsController(IUserService userService) {
        this.userService = userService;
    }

    @ModelAttribute(name = "user")
    public User user(Principal principal) {
        return userService.findByPrincipal(principal);
    }
}
