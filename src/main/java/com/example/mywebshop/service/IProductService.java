package com.example.mywebshop.service;

import com.example.mywebshop.dto.ValidProduct;
import com.example.mywebshop.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    /**
     * Parses and creates a new product and add it to the database.
     * @return id of a new product
     */
    Long addNewProduct(ValidProduct validProduct);

    /**
     * Get all the product categories as strings.
     */
    List<String> getMajorCategoriesList();

    void deleteProduct(Long id);

    /**
     * Checks all the product reviews and updates rating based on average value of all reviews for that product.
     */
    void updateProductRatingFromReviews(Long productId);

    /**
     * Updates transient fields representing pos and neg vote count of a product to help fetch them directly.
     * @param product
     */
    void initProductReviewVotes(Product product);

    Product getByIdOrThrow(Long id);

    /**
     * Method creates test random products.
     * @param num number of products to gen
     */
    void createRandomProducts(int num);

    /**
     * Fetch products by page.
     */
    Page<Product> findAllOnPage(int page, int pageSize);
}
