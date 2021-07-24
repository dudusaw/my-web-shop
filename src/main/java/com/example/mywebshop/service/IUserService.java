package com.example.mywebshop.service;

import com.example.mywebshop.dto.ValidUser;
import com.example.mywebshop.entity.User;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

public interface IUserService extends UserDetailsService {
    User findByPrincipal(@Nullable Principal principal);

    User findByUsername(String username);

    Double calculateTotalCartPrice(User user);

    void addProductToUserCart(User user, Long productId, int num);

    void removeProductFromUserCart(User user, Long productId);

    void registerUser(ValidUser validUser);

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
}
