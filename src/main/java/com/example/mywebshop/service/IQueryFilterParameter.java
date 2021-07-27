package com.example.mywebshop.service;

public interface IQueryFilterParameter {
    String alias = "prd";

    String joinPart();
    String wherePart();
    String orderByPart();
}
