package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CartProduct {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer count;

    public CartProduct(User user, Product product, Integer count) {
        this.user = user;
        this.product = product;
        this.count = count;
    }
}
