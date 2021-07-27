package com.example.mywebshop.service;

import com.example.mywebshop.dto.ValidReview;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.entity.User;
import org.springframework.validation.BindingResult;

public interface IProductReviewService {
    ProductReview getReviewById(Long reviewId);

    void deleteReview(Long reviewId, User user);

    void checkAndSubmitReview(Long productId, User user, ValidReview validReview, BindingResult bindingResult);

    void submitReviewVote(Long reviewId, User user, boolean positive);
}
