package com.example.mywebshop.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class SharedModelsController {

    @ModelAttribute(name = "username")
    public String username(Principal principal) {
        return principal == null ? null : principal.getName();
    }
}
