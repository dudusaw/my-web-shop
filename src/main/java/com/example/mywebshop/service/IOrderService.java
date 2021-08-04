package com.example.mywebshop.service;

import com.example.mywebshop.entity.Order;
import com.example.mywebshop.entity.User;

public interface IOrderService {
    Order createOrderFromCurrentUserCart(User user);

    void clearUserCart(User user);
}
