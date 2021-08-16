package com.example.mywebshop.controller;

import com.example.mywebshop.dto.ValidProduct;
import com.example.mywebshop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IProductService productService;

    @Autowired
    public AdminController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/panel")
    public String adminPanel() {
        return "admin-panel";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/add-product")
    public String addProductPanel(Model ui, ValidProduct validProduct) {
        ui.addAttribute("categoryList", productService.getMajorCategoriesList());
        return "add-product-panel";
    }

    @PostMapping("/add-product")
    public String addProduct(Model ui,
                             @RequestParam(value = "image") List<MultipartFile> images,
                             @Valid @ModelAttribute ValidProduct validProduct,
                             BindingResult result) {
        ui.addAttribute("categoryList", productService.getMajorCategoriesList());
        if (result.hasErrors()) {
            ui.addAttribute("success", false);
            ui.addAttribute("bindingResult", result);
            return "add-product-panel";
        }
        ui.addAttribute("success", true);
        validProduct.setImageFiles(images);
        productService.addNewProduct(validProduct);
        return "add-product-panel";
    }

    @GetMapping(value = "/rand/{num}")
    public String addRandomProducts(@PathVariable int num,
                                    HttpServletRequest request) {
        productService.createRandomProducts(num);
        return "redirect:/products";
    }
}
