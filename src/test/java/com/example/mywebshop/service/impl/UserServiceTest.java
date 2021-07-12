package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.validation.ValidUser;
import com.example.mywebshop.entity.CartProduct;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.repository.UserRepository;
import com.example.mywebshop.service.IUserService;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;

@SpringBootTest
@Transactional
class UserServiceTest {

    private IUserService userService;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceTest(IUserService userService,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    void userPersistInDbAfterSuccessRegister() {
        String email = "asd@gmail.com";
        String test_user = "test_user";
        String password = "asd";
        ValidUser user = new ValidUser();
        user.setEmail(email);
        user.setUsername(test_user);
        user.setPassword(password);
        user.setPasswordMatch(password);

        userService.registerUser(user);

        User tested_user = userRepository.findByUsername(test_user);
        Assertions.assertThat(tested_user).isNotNull();
        Assertions.assertThat(tested_user.getEmail()).isEqualTo(email);
        Assertions.assertThat(passwordEncoder.matches(password, tested_user.getPassword())).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,1000000,Integer.MAX_VALUE})
    void productIsOnCartAfterAdd(int num) {
        Product product = new Product();
        product.setTitle("p1");
        User user = createTestUser();
        product = productRepository.save(product);
        user = userRepository.save(user);

        userService.addProductToUserCart(user, product.getId(), num);

        Integer count = user.getCartProducts().get(product.getId()).getCount();
        Assertions.assertThat(count).isEqualTo(num);
    }

    @Test
    void productIsNotOnCartAfterRemove() {
        Product product = new Product();
        product.setTitle("p1");
        product = productRepository.save(product);

        User user = createTestUser();
        CartProduct cartProduct = new CartProduct(user, product, 1);
        user.getCartProducts().put(product.getId(), cartProduct);
        user = userRepository.save(user);

        Assertions.assertThat(user.getCartProducts().isEmpty()).isFalse();

        userService.removeProductFromUserCart(user, product.getId());

        Assertions.assertThat(user.getCartProducts().isEmpty()).isTrue();
    }

    @RepeatedTest(20)
    void calculateCartPriceTest() {
        User user = createTestUser();

        BigDecimal total = new BigDecimal(0);
        for (int i = 0; i < 100; i++) {
            Product product = new Product();
            product.setTitle("p" + i);
            BigDecimal price = BigDecimal.valueOf(RandomUtils.nextDouble(0.1, 1000000));
            int count = RandomUtils.nextInt(1, 500);
            product.setPrice(price);
            product = productRepository.save(product);
            user.getCartProducts().put(product.getId(), new CartProduct(user, product, count));
            BigDecimal contMulPrice = price.multiply(BigDecimal.valueOf(count));
            total = total.add(contMulPrice);
        }
        user = userRepository.save(user);
        Double calculatedTotalCartPrice = userService.calculateTotalCartPrice(user);

        Assertions.assertThat(calculatedTotalCartPrice).isEqualTo(total.doubleValue());
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername("test_user");
        user.setCartProducts(new HashMap<>());
        return user;
    }
}