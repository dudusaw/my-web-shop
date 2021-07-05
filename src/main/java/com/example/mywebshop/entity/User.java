package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
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

    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @MapKeyJoinColumn(name = "product_id")
    private Map<Long, CartProduct> cartProducts;

    @ManyToMany
    @JoinTable(
            name = "user_to_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRole> roles;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        cartProducts = new HashMap<>();
        roles = new ArrayList<>();
    }
}
