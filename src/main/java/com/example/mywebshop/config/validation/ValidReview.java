package com.example.mywebshop.config.validation;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ValidReview {

    @NotNull
    @Range(min = 1, max = 5)
    private Integer rating;

    @NotNull
    @Size(min = 3)
    private String review;
}
