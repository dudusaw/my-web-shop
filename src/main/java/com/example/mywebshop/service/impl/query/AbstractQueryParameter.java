package com.example.mywebshop.service.impl.query;

import com.example.mywebshop.service.IQueryFilterParameter;

import static com.example.mywebshop.utils.Util.concat;

public class AbstractQueryParameter implements IQueryFilterParameter {

    // these 3 fields should be null if the input was null or incorrect
    protected String joinPart;
    protected String wherePart;
    protected String orderByPart;

    protected boolean isInputInvalid(Object... objs) {
        for (Object obj : objs) {
            if (obj == null || obj.toString().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected String join(String tableName, String joinAlias, String referenceKeyName, String joinedKeyName) {
        return concat("inner join ", tableName, " ", joinAlias, " on (", alias, ".", referenceKeyName, " = ", joinAlias, ".", joinedKeyName, ")");
    }

    @Override
    public String joinPart() {
        return joinPart;
    }

    @Override
    public final String wherePart() {
        return wherePart;
    }

    @Override
    public final String orderByPart() {
        return orderByPart;
    }
}
