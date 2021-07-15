package com.example.mywebshop.repository;

import com.example.mywebshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select count(prv) " +
            "from ProductReviewVote prv " +
            "where prv.review.id = :id " +
            "and prv.positive = true")
    Integer getPositiveVoteCount(@Param("id") Long reviewId);

    List<Product> findAllByTitle(String title);
}
