package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.ProductNotFoundException;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductMajorCategory;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.repository.ProductMajorCategoryRepository;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.service.IProductService;
import com.example.mywebshop.service.ITextGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductMajorCategoryRepository categoryRepository;
    private final ITextGenerator textGenerator;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductMajorCategoryRepository categoryRepository,
                          ITextGenerator textGenerator) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.textGenerator = textGenerator;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateProductRatingFromReviews(Long id) {
        Product product = getByIdOrThrow(id);
        int reviewCount = product.getReviews().size();
        double ratingSum = product
                .getReviews()
                .stream()
                .mapToInt(ProductReview::getRating)
                .sum();
        product.setRating(ratingSum / reviewCount);
        productRepository.save(product);
    }

    @Override
    public void initProductReviewVotes(Product product) {
        for (ProductReview review : product.getReviews()) {
            int allVotesCount = review.getVotes().size();
            int positiveVoteCount = productRepository.getPositiveVoteCount(review.getId());
            int negativeVoteCount = allVotesCount - positiveVoteCount;
            review.setPositiveVoteCount(positiveVoteCount);
            review.setNegativeVoteCount(negativeVoteCount);
        }
    }

    @Override
    public Product getByIdOrThrow(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public void createRandomProducts(int num) {
        long[] categoryIds = categoryRepository.findAll().stream().mapToLong(ProductMajorCategory::getId).toArray();
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            long categoryId = categoryIds[RandomUtils.nextInt(0, categoryIds.length)];
            ProductMajorCategory category = categoryRepository.findById(categoryId).orElseThrow();
            Product newProduct = generateProduct(i, category);
            productList.add(newProduct);
        }
        productRepository.saveAll(productList);
    }

    private Product generateProduct(int i, ProductMajorCategory category) {
        String title = "Product " + i + RandomStringUtils.randomAlphabetic(2, 2);
        String rndDescription = textGenerator.generateText(1, TextGenLength.SHORT);
        rndDescription = rndDescription.substring(0, Math.min(rndDescription.length(), 220)).trim() + ".";
        double rating = RandomUtils.nextDouble(1, 5);
        double price = RandomUtils.nextDouble(1, 500);
        rating = BigDecimal.valueOf(rating).setScale(1, RoundingMode.HALF_UP).doubleValue();
        price = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return new Product(title, rndDescription, rating, price, category);
    }

    @Override
    public Page<Product> findAllOnPage(int page, int pageSize) {
        return productRepository.findAll(Pageable.ofSize(pageSize).withPage(page));
    }
}
