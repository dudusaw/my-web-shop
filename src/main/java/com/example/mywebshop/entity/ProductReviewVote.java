package com.example.mywebshop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class ProductReviewVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User who left the vote
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    /**
     * The review being voted by the user
     */
    @ManyToOne
    @JoinColumn(name = "review_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProductReview review;

    // like or dislike
    private boolean positive;

    public ProductReviewVote(User user, ProductReview review, boolean positive) {
        this.user = user;
        this.review = review;
        this.positive = positive;
    }
}
