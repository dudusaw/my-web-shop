package com.example.mywebshop.controller;

import com.example.mywebshop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IProductService productService;

    @Autowired
    public AdminController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/rand/{num}")
    public String addRandomProducts(@PathVariable int num) {
        productService.createRandomProducts(num);
        return "redirect:/products";
    }

    @GetMapping("/panel")
    public String adminPanel() {
        return "admin-panel";
    }
}
