package com.example.mywebshop.service.impl;

public enum OrderStatus {
    ACTIVE("active", "#00e639"),
    IN_PROGRESS("in progress", "#0073e6"),
    DONE("done", "#b366ff"),
    CANCELLED("cancelled", "#b35900");

    public final String name;
    public final String hexColor;

    OrderStatus(String name, String hexColor) {
        this.name = name;
        this.hexColor = hexColor;
    }

    @Override
    public String toString() {
        return name;
    }
}
