package com.example.mywebshop.service;

import com.example.mywebshop.entity.Order;
import com.example.mywebshop.entity.User;

public interface IOrderService {
    void setStatus(Long orderId, String status);

    Order createOrderFromCurrentUserCart(User user);

    void clearUserCart(User user);
}
