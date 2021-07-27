package com.example.mywebshop.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class ValidReview {

    @NotNull
    @Range(min = 1, max = 5)
    private Integer rating;

    @NotNull
    private String review;
}
