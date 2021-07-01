package com.example.mywebshop.service.impl;

public enum TextGenLength {
    SHORT("short"),
    MEDIUM("medium"),
    LONG("long"),
    VERY_LONG("verylong")
    ;

    public final String length;

    TextGenLength(String length) {
        this.length = length;
    }
}
