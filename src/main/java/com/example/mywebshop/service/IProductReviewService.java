package com.example.mywebshop.service;

import com.example.mywebshop.dto.ValidReview;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.entity.User;
import org.springframework.validation.BindingResult;

public interface IProductReviewService {
    ProductReview getReviewById(Long reviewId);

    void deleteReview(Long reviewId, User user);

    /**
     * Creates new review for the product and also checks for validness.
     * Returns any errors into bindingResult as strings if any.
     */
    void checkAndSubmitReview(Long productId, User user, ValidReview validReview, BindingResult bindingResult);

    /**
     * Creates new review vote and assigns it to the review.
     * Any single user could submit no more than 1 vote for each review.
     * @param positive all reviews could be either positive or negative.
     */
    void submitReviewVote(Long reviewId, User user, boolean positive);
}
