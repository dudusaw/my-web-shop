package com.example.mywebshop.service;

import com.example.mywebshop.entity.Product;
import org.springframework.data.domain.Page;

public interface IProductService {
    void deleteProduct(Long id);

    void initProductReviewVotes(Product product);

    Product getById(Long id);

    void createRandomProducts(int num);

    Page<Product> findAllOnPage(int page, int pageSize);
}
