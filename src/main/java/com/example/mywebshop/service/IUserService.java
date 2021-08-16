package com.example.mywebshop.service;

import com.example.mywebshop.dto.ValidUser;
import com.example.mywebshop.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.security.Principal;

public interface IUserService extends UserDetailsService {
    User findByPrincipal(Principal principal);

    User findByUsername(String username);

    /**
     * Calculates the total price of all products placed in user's cart.
     */
    BigDecimal calculateTotalCartPrice(User user);

    /**
     * Add num products to the user's cart.
     */
    void addProductToUserCart(User user, Long productId, int num);

    /**
     * Remove all the products from user's cart.
     */
    void removeProductFromUserCart(User user, Long productId);

    /**
     * Register a new user and store into database.
     */
    void registerUser(ValidUser validUser);

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
}
