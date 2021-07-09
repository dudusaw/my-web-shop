package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.ProductNotFoundException;
import com.example.mywebshop.config.validation.ValidUser;
import com.example.mywebshop.entity.*;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.repository.UserRepository;
import com.example.mywebshop.repository.UserRoleRepository;
import com.example.mywebshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       ProductRepository productRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
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
        double price = 0.0;
        for (CartProduct cartProduct : user.getCartProducts().values()) {
            Product product = cartProduct.getProduct();
            price += product.getPrice() * cartProduct.getCount();
        }
        return price;
    }

    @Override
    public void addProductToUserCart(User user, Long productId, int num) {
        Product product = findProductOrThrow(productId);
        CartProduct cartProduct = user.getCartProducts().get(productId);
        if (cartProduct == null) {
            cartProduct = new CartProduct(user, product, 0);
            user.getCartProducts().put(productId, cartProduct);
        }
        cartProduct.setCount(cartProduct.getCount() + 1);
    }

    @Override
    public void removeProductFromUserCart(User user, Long productId) {
        CartProduct productInCart = user.getCartProducts().remove(productId);
        em.remove(productInCart);
    }

    @Override
    public void registerUser(ValidUser validUser) {
        String passwordEncoded = passwordEncoder.encode(validUser.getPassword());
        User user = new User(validUser.getEmail(), validUser.getUsername(), passwordEncoded);
        UserRole defaultRole = userRoleRepository.findByName("USER");
        user.getRoles().add(defaultRole);
        userRepository.save(user);
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
