package com.example.mywebshop.repository;

import com.example.mywebshop.entity.ProductReviewVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewVoteRepository extends JpaRepository<ProductReviewVote, Long> {

    Optional<ProductReviewVote> findByUserIdAndReviewId(Long userId, Long reviewId);
}
