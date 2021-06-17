package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.ProductNotFoundException;
import com.example.mywebshop.entity.CartProduct;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductCategory;
import com.example.mywebshop.repository.ProductCategoryRepository;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.service.IProductService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService implements IProductService {

    @PersistenceContext
    private EntityManager em;

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        for (CartProduct cartProduct : product.getCartProducts()) {
            em.remove(cartProduct);
        }
        em.remove(product);
    }

    @Override
    public void createRandomProducts(int num) {
        long[] categoryIds = categoryRepository.findAll().stream().mapToLong(ProductCategory::getId).toArray();
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            String title = "Product " + i + RandomStringUtils.randomAlphabetic(1, 3);
            String rndDescription = RandomStringUtils.randomAlphabetic(100, 200);
            double rating = RandomUtils.nextDouble(1, 5);
            double price = RandomUtils.nextDouble(1, 500);
            rating = BigDecimal.valueOf(rating).setScale(2, RoundingMode.HALF_UP).doubleValue();
            price = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
            long categoryId = categoryIds[RandomUtils.nextInt(0, categoryIds.length)];
            ProductCategory category = categoryRepository.findById(categoryId).orElseThrow();
            Product newProduct = new Product(title, rndDescription, rating, price, category);
            productList.add(newProduct);
        }
        productRepository.saveAll(productList);
    }

    @Override
    public Page<Product> findAllOnPage(int page, int pageSize) {
        return productRepository.findAll(Pageable.ofSize(pageSize).withPage(page));
    }
}
