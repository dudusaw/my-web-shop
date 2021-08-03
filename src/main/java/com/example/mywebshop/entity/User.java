package com.example.mywebshop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "user_entity") // user is already defined keyword in pg
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @MapKeyJoinColumn(name = "product_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Map<Long, CartProduct> cartProducts;

    @ManyToMany
    @JoinTable(
            name = "user_to_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<UserRole> roles;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        cartProducts = new HashMap<>();
        roles = new ArrayList<>();
    }

    public boolean hasRole(String role) {
        for (UserRole userRole : roles) {
            if (userRole.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public int getCartProductsCount() {
        int c = 0;
        for (CartProduct value : cartProducts.values()) {
            c += value.getCount();
        }
        return c;
    }
}
