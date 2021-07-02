package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class ProductReviewVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private ProductReview review;

    // like or dislike
    private boolean positive;

    public ProductReviewVote(User user, ProductReview review, boolean positive) {
        this.user = user;
        this.review = review;
        this.positive = positive;
    }
}
