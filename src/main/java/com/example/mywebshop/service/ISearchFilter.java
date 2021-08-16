package com.example.mywebshop.service;

import com.example.mywebshop.dto.SearchFilterInfo;
import com.example.mywebshop.entity.Product;

import java.util.List;

public interface ISearchFilter {
    /**
     * Activate search parameter.
     */
    void addParameter(IQueryFilterParameter parameter);

    /**
     * Deactivate search parameter.
     */
    void removeParameter(Class<? extends IQueryFilterParameter> parameterType);

    /**
     * Deactivate search parameter.
     */
    void removeParameter(IQueryFilterParameter parameter);

    /**
     * Deactivate all search parameters.
     */
    void clearFilters();

    /**
     * Set sort parameter to specified type.
     */
    void sortBy(Class<? extends IQueryFilterParameter> parameterType, boolean ascending);

    /**
     * Set all parameters from parsed values.
     */
    void parseAndSetParameters(SearchFilterInfo params);

    /**
     * Get last used parameters or empty if no of them were used before.
     */
    SearchFilterInfo getLastParams();

    /**
     * Create and execute a query, return a result of products with all filters applied with default page size.
     * @return
     */
    List<Product> getFiltered();

    /**
     * Returns list of products that matches active filter parameters.
     * Returns all products if no filters active.
     *
     * @param pageSize how many items on 1 page
     * @param pageNum  starts from 1
     */
    List<Product> getFiltered(int pageSize, int pageNum);
}
