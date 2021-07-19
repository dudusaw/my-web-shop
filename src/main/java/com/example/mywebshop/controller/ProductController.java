package com.example.mywebshop.controller;

import com.example.mywebshop.config.validation.ValidReview;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IProductService;
import com.example.mywebshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;
    private final IUserService userService;

    @Autowired
    public ProductController(IProductService productService, IUserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String products(Model ui,
                           @NotNull Principal principal,
                           @RequestParam(defaultValue = "0") int pageNum,
                           @RequestParam(defaultValue = "30") int pageSize) {
        Page<Product> page = productService.findAllOnPage(pageNum, pageSize);
        User user = userService.findByPrincipal(principal);
        ui.addAttribute("user", user);
        ui.addAttribute("products", page);
        return "product-list";
    }

    @GetMapping("/{productId}")
    public String productDetails(@PathVariable Long productId, Model ui) {
        Product product = productService.getByIdOrThrow(productId);
        productService.initProductReviewVotes(product);
        ui.addAttribute("product", product);
        return "product-details";
    }

    @PostMapping("/act/submit-review/{productId}")
    public String submitReview(Model ui,
                               @PathVariable Long productId,
                               @Valid ValidReview validReview,
                               BindingResult bindingResult,
                               @NotNull Principal principal) {
        User user = userService.findByPrincipal(principal);
        Product product = productService.getByIdOrThrow(productId);
        productService.submitReview(productId, user, validReview, bindingResult);
        ui.addAttribute("bindingResult", bindingResult);
        ui.addAttribute("product", product);
        return "redirect:/products/"+productId;
    }

    @GetMapping("/act/delete-review/{productId}/{reviewId}")
    public String deleteReview(@PathVariable Long productId,
                               @PathVariable Long reviewId,
                               @NotNull Principal principal) {
        User user = userService.findByPrincipal(principal);
        productService.deleteReview(reviewId, user);
        return "redirect:/products/"+productId;
    }

    @GetMapping("/act/vote-positive/{productId}/{reviewId}")
    public String reviewSubmitPositiveVote(@PathVariable Long productId,
                                           @PathVariable Long reviewId,
                                           @NotNull Principal principal) {
        User user = userService.findByPrincipal(principal);
        productService.submitReviewVote(reviewId, user, true);
        return "redirect:/products/"+productId;
    }

    @GetMapping("/act/vote-negative/{productId}/{reviewId}")
    public String reviewSubmitNegativeVote(@PathVariable Long productId,
                                           @PathVariable Long reviewId,
                                           @NotNull Principal principal) {
        User user = userService.findByPrincipal(principal);
        productService.submitReviewVote(reviewId, user, false);
        return "redirect:/products/"+productId;
    }
}
