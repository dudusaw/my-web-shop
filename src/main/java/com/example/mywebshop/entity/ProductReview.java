package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer rating;

    private String review;

    private LocalDateTime timestamp = LocalDateTime.now();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ProductReviewVote> votes;

    @Transient
    private int positiveVoteCount;

    @Transient
    private int negativeVoteCount;

    public ProductReview(User user, Product product, Integer rating, String review) {
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.review = review;
    }
}
