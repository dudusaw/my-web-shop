package com.example.mywebshop.service;

import com.example.mywebshop.config.validation.ValidUser;
import com.example.mywebshop.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpSession;

public interface IUserService extends UserDetailsService {
    User findByUsername(String username);

    User getUserFromSession(HttpSession session);

    Double calculateTotalCartPrice(User user);

    void addProductToUserCart(User user, Long productId, int num);

    void removeProductFromUserCart(User user, Long productId);

    void registerUser(ValidUser validUser);

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
}
