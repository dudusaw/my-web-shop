package com.example.mywebshop.service.impl.query;

import static com.example.mywebshop.utils.Util.concat;

public class PriceRangeParameter extends AbstractQueryParameter {

    /**
     * Both inclusive
     */
    public PriceRangeParameter(Integer minPrice, Integer maxPrice) {
        minPrice = (minPrice == null) ? 0 : minPrice;
        maxPrice = (maxPrice == null || maxPrice == 0) ? Integer.MAX_VALUE : maxPrice;
        // p.price between 5 and 100
        wherePart = concat(alias, ".price between ", String.valueOf(minPrice), " and ", String.valueOf(maxPrice));
        orderByPart = concat(alias, ".price");
    }
}
