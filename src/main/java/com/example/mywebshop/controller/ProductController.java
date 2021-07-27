package com.example.mywebshop.controller;

import com.example.mywebshop.dto.SearchFilterInfo;
import com.example.mywebshop.dto.ValidReview;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IProductReviewService;
import com.example.mywebshop.service.IProductService;
import com.example.mywebshop.service.ISearchFilter;
import com.example.mywebshop.service.IUserService;
import com.example.mywebshop.service.impl.query.MinimalRatingParameter;
import com.example.mywebshop.service.impl.query.PriceRangeParameter;
import com.example.mywebshop.utils.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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
                           @RequestParam(defaultValue = "30") int pageSize) {
        List<Product> page = searchFilter.getFiltered();
        List<String> categoriesList = productService.getMajorCategoriesList();
        categoriesList.add(0, "all");
        User user = userService.findByPrincipal(principal);
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

    @PostMapping("/clear-filters")
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
