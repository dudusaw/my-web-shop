package com.example.mywebshop.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

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

    private List<MultipartFile> imageFiles;

    @NotNull(message = "price is empty")
    private BigDecimal price;

    @NotNull(message = "category is empty")
    private String category;
}
