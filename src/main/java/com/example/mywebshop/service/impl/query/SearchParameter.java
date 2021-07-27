package com.example.mywebshop.service.impl.query;

import static com.example.mywebshop.utils.Util.concat;

public class SearchParameter extends AbstractQueryParameter {

    public SearchParameter(String searchQuery) {
        if (isInputInvalid(searchQuery)) return;
        // lower('p.title') like lower('%query%')
        wherePart = concat("lower(", alias, ".title) like '%", searchQuery.toLowerCase(), "%'");
        orderByPart = concat(alias, ".title");
    }
}