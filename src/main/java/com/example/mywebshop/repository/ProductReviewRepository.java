package com.example.mywebshop.repository;

import com.example.mywebshop.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    Optional<ProductReview> findByUserIdAndProductId(Long userId, Long productId);
}
