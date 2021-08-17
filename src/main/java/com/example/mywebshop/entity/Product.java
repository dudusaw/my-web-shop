package com.example.mywebshop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.tuple.Pair;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String shortDescription;

    private String description;

    private String characteristics;

    @Column(precision = 4, scale = 2)
    private Double rating;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToMany
    @JoinTable(
            name = "product_to_file",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "file_meta_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FileMeta> imageFiles;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProductMajorCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CartProduct> cartProducts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ProductReview> reviews;

    public Product(String title, String shortDescription, String description, String characteristics, Double rating, BigDecimal price, ProductMajorCategory category) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.description = description;
        this.characteristics = characteristics;
        this.rating = rating;
        this.price = price;
        this.category = category;
    }

    public List<Pair<String, String>> getCharacteristicList() {
        List<Pair<String, String>> list = new ArrayList<>();
        if (characteristics == null) return list;
        String[] lines = characteristics.split("\n");
        for (String line : lines) {
            int firstColon = line.indexOf(":");
            String key = line.substring(0, firstColon);
            String value = line.substring(firstColon+1).trim();
            list.add(Pair.of(key, value));
        }
        return list;
    }
}
