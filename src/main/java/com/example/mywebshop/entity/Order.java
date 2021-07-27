package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "order_entity")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "default now()")
    private LocalDateTime timestamp;

    @ManyToMany
    @JoinTable(
            name = "order_to_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @ToStringExclude
    private List<Product> products;

    private BigDecimal totalPrice;

    public Order(User user, List<Product> products, BigDecimal totalPrice) {
        this.user = user;
        this.products = products;
        this.totalPrice = totalPrice;
    }
}
