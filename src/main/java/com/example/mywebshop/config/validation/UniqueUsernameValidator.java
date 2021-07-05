package com.example.mywebshop.config.validation;

import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsernameConstraint, String> {

    private final IUserService userService;

    @Autowired
    public UniqueUsernameValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        User user = userService.findByUsername(s);
        return user == null; // if this null then there's no such user, then it's okay to add one with that name
    }
}
