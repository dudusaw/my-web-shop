package com.example.mywebshop.config;

import com.example.mywebshop.config.validation.ValidUser;
import com.example.mywebshop.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UserActionLogging {

    @Pointcut(value = "within(com.example.mywebshop.service.impl.UserService)")
    public void userServiceClassMethods() {}

    @Before(value = "userServiceClassMethods() && execution(* add*UserCart(..)) && args(user, productId, ..)", argNames = "user,productId")
    public void addUserCart(User user, Long productId) {
        log.info(String.format("Product{%d} added to the cart of user: %s", productId, user.getUsername()));
    }

    @Before(value = "userServiceClassMethods() && execution(* remove*UserCart(..)) && args(user, productId, ..)", argNames = "user,productId")
    public void removeFromUserCart(User user, Long productId) {
        log.info(String.format("Product{%d} removed from the cart of user: %s", productId, user.getUsername()));
    }

    @Before(value = "userServiceClassMethods() && execution(* registerUser(..)) && args(user)", argNames = "user")
    public void register(ValidUser user) {
        log.info(String.format("User{%s} registered successfully", user.getUsername()));
    }
}
