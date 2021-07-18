package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToStringExclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @ToStringExclude
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer count;

    public CartProduct(User user, Product product, Integer count) {
        this.user = user;
        this.product = product;
        this.count = count;
    }
}
