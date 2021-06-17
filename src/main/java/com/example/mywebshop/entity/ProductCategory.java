package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table
@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    @MapKey
    private Map<Long, Product> products;
}
