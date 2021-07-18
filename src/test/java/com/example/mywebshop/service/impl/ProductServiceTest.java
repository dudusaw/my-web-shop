package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.validation.ValidProduct;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.entity.ProductReviewVote;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.service.IProductService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestEntityManager
@Transactional
class ProductServiceTest {

    private IProductService productService;
    private ProductRepository productRepository;
    private TestEntityManager entityManager;

    @Autowired
    ProductServiceTest(IProductService productService, ProductRepository productRepository, TestEntityManager entityManager) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @Test
    void addNewProductTest_valid() {
        ValidProduct validProduct = new ValidProduct();
        String title = "test_product";
        String desc = "asd";
        String shortDesc = "asd";
        String category = "home"; // this from migrations
        BigDecimal price = BigDecimal.valueOf(352);
        validProduct.setTitle(title);
        validProduct.setCategory(category);
        validProduct.setPrice(price);
        validProduct.setDescription(desc);
        validProduct.setShortDescription(shortDesc);

        Long id = productService.addNewProduct(validProduct);

        Optional<Product> product = productRepository.findById(id);
        assertThat(product.isPresent()).isTrue();
        assertThat(product.get().getTitle()).isEqualTo(title);
        assertThat(product.get().getPrice()).isEqualTo(price);
    }

    @RepeatedTest(5)
    void initProductReviewVotesTest() {
        Product product = new Product();
        User user = new User();
        ProductReview review = new ProductReview(user, product, 5, "");
        product.setReviews(List.of(review));
        int totalVotes = 1000;
        int positiveVotes = 0;
        product = entityManager.persist(product);
        user = entityManager.persist(user);
        review = entityManager.persist(review);
        List<ProductReviewVote> votes = new ArrayList<>();
        for (int j = 0; j < totalVotes; j++) {
            boolean positive = RandomUtils.nextBoolean();
            positiveVotes += positive ? 1 : 0;
            ProductReviewVote vote = new ProductReviewVote(user, review, positive);
            votes.add(vote);
            entityManager.persist(vote);
        }
        review.setVotes(votes);
        entityManager.flush();
        product = entityManager.find(Product.class, product.getId());

        productService.initProductReviewVotes(product);

        review = entityManager.find(ProductReview.class, review.getId());

        assertThat(review.getPositiveVoteCount()).isEqualTo(positiveVotes);
        assertThat(review.getNegativeVoteCount()).isEqualTo(totalVotes - positiveVotes);
    }

    @RepeatedTest(5)
    void updateProductRatingFromReviewsTest() {
        Product product = productRepository.getById(1L); // from migrations
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setUsername("u"+i);
            ProductReview productReview = new ProductReview(user, product, RandomUtils.nextInt(1, 5), "");
            entityManager.persist(user);
            entityManager.persist(productReview);
        }
        entityManager.flush();
        product = entityManager.refresh(product);

        int count = product.getReviews().size();
        double ratingSum = product.getReviews().stream().mapToInt(ProductReview::getRating).sum();
        double resultRating = ratingSum / count;

        productService.updateProductRatingFromReviews(product.getId());

        assertThat(product.getRating()).isEqualTo(resultRating);
    }

    @Test
    void deleteProductTest_noSuchProductAfterDelete() {
        Product product = new Product();
        product.setTitle("asd");

        Product savedProduct = productRepository.save(product);

        productService.deleteProduct(savedProduct.getId());

        assertThat(productRepository.findById(savedProduct.getId()).isEmpty()).isTrue();
    }

    @Test
    void getByIdOrThrowTest_throwsIfNotExists() {
        assertThatThrownBy(() -> productService.getByIdOrThrow(5623675982L));
    }
}