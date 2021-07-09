package com.example.mywebshop.config.validation;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ValidProduct {

    @Size(min = 3, message = "title: min size is 3 symbols")
    @NotNull(message = "title is empty")
    private String title;

    @Size(min = 3, message = "short desc: min size is 3 symbols")
    @NotNull(message = "short description is empty")
    private String shortDescription;

    @Size(min = 3, message = "main desc: min size is 3 symbols")
    @NotNull(message = "description is empty")
    private String description;

    private MultipartFile imageFile;

    @NotNull(message = "price is empty")
    private Double price;

    @NotNull(message = "category is empty")
    private String category;
}
