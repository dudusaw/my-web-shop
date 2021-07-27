package com.example.mywebshop.service.impl.query;

import static com.example.mywebshop.utils.Util.concat;

public class CategoryParameter extends AbstractQueryParameter {

    public CategoryParameter(String category) {
        if (isInputInvalid(category)) return;
        joinPart = join("product_major_category", "pmc", "category_id", "id");
        wherePart = concat("pmc.name = '", category, "'");
        orderByPart = "pmc.name";
    }
}
