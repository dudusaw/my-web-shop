package com.example.mywebshop.controller;

import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IProductService;
import com.example.mywebshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    private final IProductService IProductService;
    private final IUserService IUserService;

    @Autowired
    public MainController(IProductService IProductService, IUserService IUserService) {
        this.IProductService = IProductService;
        this.IUserService = IUserService;
    }

    @GetMapping("/products")
    public String products(Model ui,
                           HttpServletRequest request,
                           @RequestParam(defaultValue = "0") int pageNum,
                           @RequestParam(defaultValue = "30") int pageSize) {
        Page<Product> page = IProductService.findAllOnPage(pageNum, pageSize);
        User user = IUserService.getUserFromSession(request.getSession());
        ui.addAttribute("user", user);
        ui.addAttribute("products", page);
        return "product-list";
    }

    @GetMapping("/home")
    public String home(Model ui,
                       HttpServletRequest request) {
        User user = IUserService.getUserFromSession(request.getSession());
        ui.addAttribute("user", user);
        return "home";
    }
}
