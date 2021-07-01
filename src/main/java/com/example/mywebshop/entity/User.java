package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<CartProduct> cartProducts;

    @ManyToMany
    @JoinTable(
            name = "user_to_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRole> roles;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        cartProducts = new ArrayList<>();
        roles = new ArrayList<>();
    }

    public CartProduct getProductInCart(Long productId) {
        for (CartProduct cartProduct : cartProducts) {
            if (cartProduct.getProduct().getId().equals(productId)) {
                return cartProduct;
            }
        }
        return null;
    }
}
