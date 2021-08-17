package com.example.mywebshop.controller;

import com.example.mywebshop.dto.SearchFilterInfo;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IProductService;
import com.example.mywebshop.service.ISearchFilter;
import com.example.mywebshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/products")
public class ProductController {

    private final IProductService productService;
    private final IUserService userService;

    @Resource
    private ISearchFilter searchFilter;

    @Autowired
    public ProductController(IProductService productService,
                             IUserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String products(Model ui,
                           Principal principal,
                           @RequestParam(defaultValue = "0") int pageNum,
                           @RequestParam(defaultValue = "18") int pageSize) {
        List<Product> page = searchFilter.getFiltered(pageSize, pageNum);
        List<String> categoriesList = productService.getMajorCategoriesList();
        categoriesList.add(0, "all");
        User user = userService.findByPrincipal(principal);
        ui.addAttribute("pageNum", pageNum);
        ui.addAttribute("filterParams", searchFilter.getLastParams());
        ui.addAttribute("user", user);
        ui.addAttribute("products", page);
        ui.addAttribute("categoriesList", categoriesList);
        return "product-list";
    }

    @PostMapping("/apply-filters")
    public String applyFilters(SearchFilterInfo searchFilterInfo) {
        searchFilter.parseAndSetParameters(searchFilterInfo);
        return "redirect:/products";
    }

    @RequestMapping(value = "/clear-filters", method = {RequestMethod.POST, RequestMethod.GET})
    public void clearFilters() {
        searchFilter.clearFilters();
    }

    @GetMapping("/{productId}")
    public String productDetails(@PathVariable Long productId,
                                 Model ui) {
        Product product = productService.getByIdOrThrow(productId);
        productService.initProductReviewVotes(product);
        ui.addAttribute("product", product);
        return "product-details";
    }
}
