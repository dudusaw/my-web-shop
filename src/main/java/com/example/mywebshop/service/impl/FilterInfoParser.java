package com.example.mywebshop.service.impl;

import com.example.mywebshop.dto.SearchFilterInfo;
import com.example.mywebshop.service.IQueryFilterParameter;
import com.example.mywebshop.service.impl.query.CategoryParameter;
import com.example.mywebshop.service.impl.query.MinimalRatingParameter;
import com.example.mywebshop.service.impl.query.PriceRangeParameter;
import com.example.mywebshop.service.impl.query.SearchParameter;

import java.util.ArrayList;
import java.util.List;

public class FilterInfoParser {

    private SearchFilterInfo params = new SearchFilterInfo();

    public SearchFilterInfo getLastParams() {
        return params;
    }

    public List<IQueryFilterParameter> parse(SearchFilterInfo params) {
        this.params = params;
        List<IQueryFilterParameter> list = new ArrayList<>();
        String searchQuery = params.getSearchQuery();
        Integer maxPrice = params.getMaxPrice();
        Integer minPrice = params.getMinPrice();
        String category = params.getCategory();
        Double minRating = params.getMinRating();
        if (isValid(searchQuery)) {
            list.add(new SearchParameter(searchQuery));
        }
        if (isValid(minPrice, maxPrice)) {
            list.add(new PriceRangeParameter(minPrice, maxPrice));
        }
        if (isValid(minRating)) {
            list.add(new MinimalRatingParameter(minRating));
        }
        if (isValid(category) && !category.equals("all")) {
            list.add(new CategoryParameter(category));
        }
        return list;
    }

    private boolean isValid(Object... args) {
        for (Object arg : args) {
            if (arg != null && !arg.toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
