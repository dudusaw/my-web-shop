package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.validation.ValidUser;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.repository.UserRepository;
import com.example.mywebshop.service.IUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void userPersistInDbAfterSuccessRegister() {
        ValidUser user = new ValidUser();
        user.setEmail("asd@gmail.com");
        user.setUsername("test_user");
        user.setPassword("asd");
        user.setPasswordMatch("asd");

        userService.registerUser(user);

        Assertions.assertThat(userRepository.findByUsername("asd")).isNotNull();
    }
}