package com.example.mywebshop.service.impl.query;

import static com.example.mywebshop.utils.Util.concat;

public class MinimalRatingParameter extends AbstractQueryParameter {

    public MinimalRatingParameter(Double minimalRating) {
        if (isInputInvalid(minimalRating)) return;
        wherePart = concat(alias, ".rating >= ", String.valueOf(minimalRating));
        orderByPart = concat(alias, ".rating");
    }
}


