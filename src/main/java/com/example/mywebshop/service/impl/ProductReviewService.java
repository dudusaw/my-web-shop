package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.NotFoundException;
import com.example.mywebshop.dto.ValidReview;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.entity.ProductReviewVote;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.repository.ProductReviewRepository;
import com.example.mywebshop.repository.ReviewVoteRepository;
import com.example.mywebshop.service.IProductReviewService;
import com.example.mywebshop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ProductReviewService implements IProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final ReviewVoteRepository reviewVoteRepository;
    private final ProductRepository productRepository;
    private final IProductService productService;

    @Autowired
    public ProductReviewService(ProductReviewRepository productReviewRepository,
                                ReviewVoteRepository reviewVoteRepository,
                                ProductRepository productRepository,
                                IProductService productService) {
        this.productReviewRepository = productReviewRepository;
        this.reviewVoteRepository = reviewVoteRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @Override
    public ProductReview getReviewById(Long reviewId) {
        return productReviewRepository
                .findById(reviewId)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void deleteReview(Long reviewId, User user) {
        ProductReview productReview = productReviewRepository
                .findById(reviewId)
                .orElseThrow(NotFoundException::new);

        if (user.hasRole("ADMIN") || productReview
                .getUser()
                .getUsername()
                .equals(user.getUsername())) {
            productReviewRepository.deleteById(productReview.getId());
            productReviewRepository.flush();
            productService.updateProductRatingFromReviews(productReview.getProduct().getId());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "no rights for that");
        }
    }

    @Override
    public void checkAndSubmitReview(Long productId, User user, ValidReview validReview, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return;
        Product product = productRepository
                .findById(productId)
                .orElseThrow(NotFoundException::new);

        Optional<ProductReview> persistedReview = productReviewRepository.findByUserIdAndProductId(user.getId(), productId);
        if (persistedReview.isPresent()) {
            bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "review already exists by this user"));
            return;
        }
        ProductReview newReview = new ProductReview(user, product, validReview.getRating(), validReview.getReview());
        productReviewRepository.save(newReview);
        productService.updateProductRatingFromReviews(productId);
    }

    @Override
    public void submitReviewVote(Long reviewId, User user, boolean positive) {
        ProductReview productReview = productReviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no such review " + reviewId));
        Optional<ProductReviewVote> persistedVote = reviewVoteRepository.findByUserIdAndReviewId(user.getId(), productReview.getId());
        if (persistedVote.isPresent()) {
            ProductReviewVote vote = persistedVote.get();
            if (vote.isPositive() == positive) {
                reviewVoteRepository.delete(vote);
            } else {
                vote.setPositive(!vote.isPositive());
            }
        } else {
            ProductReviewVote vote = new ProductReviewVote(user, productReview, positive);
            reviewVoteRepository.save(vote);
        }
    }
}
