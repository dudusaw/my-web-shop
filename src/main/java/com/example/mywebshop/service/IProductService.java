package com.example.mywebshop.service;

import com.example.mywebshop.dto.ValidProduct;
import com.example.mywebshop.dto.ValidReview;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface IProductService {
    Long addNewProduct(ValidProduct validProduct);

    List<String> getMajorCategoriesList();

    void deleteProduct(Long id);

    void submitReview(Long productId, User user, ValidReview validReview, BindingResult bindingResult);

    void deleteReview(Long reviewId, User user);

    /**
     * Add new vote to the review, deletes one if already present by this user.
     */
    void submitReviewVote(Long reviewId, User user, boolean positive);

    void updateProductRatingFromReviews(Long id);

    void initProductReviewVotes(Product product);

    Product getByIdOrThrow(Long id);

    void createRandomProducts(int num);

    Page<Product> findAllOnPage(int page, int pageSize);
}
