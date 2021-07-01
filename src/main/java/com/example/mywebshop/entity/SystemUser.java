package com.example.mywebshop.entity;

import com.example.mywebshop.config.valid.FieldMatchConstraint;
import com.example.mywebshop.config.valid.UniqueUsernameConstraint;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldMatchConstraint(message = "Password doesn't match", field = "password", fieldMatch = "passwordMatch")
public class SystemUser {

    @NotNull
    @UniqueUsernameConstraint
    @Size(min = 3, message = "min username length is 3 symbols")
    private String username;

    @NotNull
    @Size(min = 3, message = "email is invalid")
    @Email(message = "email is invalid")
    private String email;

    @NotNull
    @Size(min = 3, message = "min password length is 3 symbols")
    private String password;

    @NotNull
    @Size(min = 3, message = "min password length is 3 symbols")
    private String passwordMatch;
}
