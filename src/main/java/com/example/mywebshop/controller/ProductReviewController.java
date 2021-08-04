package com.example.mywebshop.controller;

import com.example.mywebshop.dto.ValidReview;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IProductReviewService;
import com.example.mywebshop.service.IProductService;
import com.example.mywebshop.service.IUserService;
import com.example.mywebshop.utils.Util;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductReviewController {

    private final IProductService productService;
    private final IProductReviewService productReviewService;
    private final IUserService userService;

    @Autowired
    public ProductReviewController(IProductService productService,
                             IProductReviewService productReviewService,
                             IUserService userService) {
        this.productService = productService;
        this.productReviewService = productReviewService;
        this.userService = userService;
    }

    @PostMapping("/act/submit-review/{productId}")
    public ResponseEntity<String> submitReview(@PathVariable Long productId,
                                               @Valid ValidReview validReview,
                                               BindingResult bindingResult,
                                               Principal principal) {
        User user = userService.findByPrincipal(principal);
        productReviewService.checkAndSubmitReview(productId, user, validReview, bindingResult);
        List<String> errorMessages = bindingResult
                .getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());
        String responseJson = Util.mapToJson(
                Pair.of("submitSuccess", String.valueOf(!bindingResult.hasErrors())),
                Pair.of("errorMessages", errorMessages)
        );
        return ResponseEntity.ok(responseJson);
    }

    @GetMapping("/act/delete-review/{productId}/{reviewId}")
    public String deleteReview(@PathVariable Long productId,
                               @PathVariable Long reviewId,
                               Principal principal) {
        User user = userService.findByPrincipal(principal);
        productReviewService.deleteReview(reviewId, user);
        return "redirect:/products/" + productId;
    }

    @GetMapping("/act/vote/{productId}/{reviewId}/{positive}")
    public ResponseEntity<String> reviewSubmitVote(@PathVariable Long productId,
                                                   @PathVariable Long reviewId,
                                                   @PathVariable Boolean positive,
                                                   Principal principal) {
        User user = userService.findByPrincipal(principal);
        productReviewService.submitReviewVote(reviewId, user, positive);
        Product product = productService.getByIdOrThrow(productId);
        productService.initProductReviewVotes(product);
        ProductReview review = productReviewService.getReviewById(reviewId);
        String response = Util.mapToJson(
                Pair.of("positiveCount", review.getPositiveVoteCount()),
                Pair.of("negativeCount", review.getNegativeVoteCount())
        );
        return ResponseEntity.ok(response);
    }
}
