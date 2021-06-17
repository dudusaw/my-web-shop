package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(precision = 4, scale = 2)
    private Double rating;

    @Column(precision = 12, scale = 5)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToMany(mappedBy = "product")
    private List<CartProduct> cartProducts;

    public Product(String title, String description, Double rating, Double price, ProductCategory category) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.category = category;
    }
}
