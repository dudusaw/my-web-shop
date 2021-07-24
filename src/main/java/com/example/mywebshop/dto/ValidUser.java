package com.example.mywebshop.dto;

import com.example.mywebshop.config.validation.FieldMatchConstraint;
import com.example.mywebshop.config.validation.UniqueUsernameConstraint;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Dto class for validation user before registering.
 */
@Data
@FieldMatchConstraint(message = "Password doesn't match", field = "password", fieldMatch = "passwordMatch")
public class ValidUser {

    @NotNull
    @UniqueUsernameConstraint
    @Pattern(regexp = "[a-zA-Z][\\w-]+", message = "first symbol should be letter, then numbers and _ - symbols allowed")
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
