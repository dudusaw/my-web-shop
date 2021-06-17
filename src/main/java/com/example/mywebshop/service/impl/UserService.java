package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.ProductNotFoundException;
import com.example.mywebshop.entity.*;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.repository.UserRepository;
import com.example.mywebshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service
@Transactional
public class UserService implements IUserService {

    @PersistenceContext
    private EntityManager em;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public UserService(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserFromSession(HttpSession session) {
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        if (userDetails == null) {
            return null;
        }
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @Override
    public Double calculateTotalCartPrice(User user) {
        Double price = 0.0;
        for (CartProduct cartProduct : user.getCartProducts()) {
            Product product = cartProduct.getProduct();
            price += product.getPrice() * cartProduct.getCount();
        }
        return price;
    }

    @Override
    public void addProductToUserCart(User user, Long productId, int num) {
        Product product = findProductOrThrow(productId);
        CartProduct cartProduct = user.getProductInCart(productId);
        if (cartProduct == null) {
            cartProduct = new CartProduct(user, product, 0);
            user.getCartProducts().add(cartProduct);
        }
        cartProduct.setCount(cartProduct.getCount() + 1);
    }

    @Override
    public void removeProductFromUserCart(User user, Long productId) {
        CartProduct productInCart = user.getProductInCart(productId);
        user.getCartProducts().remove(productInCart);
        em.remove(productInCart);
    }

    private Product findProductOrThrow(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException("not found " + s);
        }

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles()
                        .stream()
                        .map(UserRole::getName)
                        .toArray(String[]::new))
                .build();
    }
}
