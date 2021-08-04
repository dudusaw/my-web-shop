package com.example.mywebshop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table
@Data
@NoArgsConstructor
public class ProductMajorCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    @MapKey
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Map<Long, Product> products;
}
