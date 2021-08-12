package com.example.mywebshop.dto;

import lombok.Data;

@Data
public class SearchFilterInfo {

    private String searchQuery;

    private String category;

    private Integer minPrice;

    private Integer maxPrice;

    private Double minRating;
}
