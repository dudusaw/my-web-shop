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
        list.add(new SearchParameter(params.getSearchQuery()));
        list.add(new PriceRangeParameter(params.getMinPrice(), params.getMaxPrice()));
        list.add(new MinimalRatingParameter(params.getMinRating()));
        String category = params.getCategory();
        if (!category.equals("all")) {
            list.add(new CategoryParameter(category));
        }
        return list;
    }
}
