package com.example.mywebshop.service;

import com.example.mywebshop.dto.SearchFilterInfo;
import com.example.mywebshop.entity.Product;

import java.util.List;

public interface ISearchFilter {
    void addParameter(IQueryFilterParameter parameter);

    void removeParameter(Class<? extends IQueryFilterParameter> parameterType);

    void removeParameter(IQueryFilterParameter parameter);

    void clearFilters();

    void sortBy(Class<? extends IQueryFilterParameter> parameterType, boolean ascending);

    void parseAndSetParameters(SearchFilterInfo params);

    SearchFilterInfo getLastParams();

    List<Product> getFiltered();

    List<Product> getFiltered(int pageSize, int pageNum);
}
