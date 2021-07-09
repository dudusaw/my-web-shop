package com.example.mywebshop.service;

import com.example.mywebshop.config.validation.ValidProduct;
import com.example.mywebshop.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    void addNewProduct(ValidProduct validProduct);

    List<String> getMajorCategoriesList();

    void deleteProduct(Long id);

    void updateProductRatingFromReviews(Long id);

    void initProductReviewVotes(Product product);

    Product getByIdOrThrow(Long id);

    void createRandomProducts(int num);

    Page<Product> findAllOnPage(int page, int pageSize);
}
