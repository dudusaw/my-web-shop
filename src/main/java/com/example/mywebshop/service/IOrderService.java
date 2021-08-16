package com.example.mywebshop.service;

import com.example.mywebshop.entity.Order;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.impl.OrderStatus;

public interface IOrderService {
    /**
     * Set new current status of an order
     * @param status this should exactly match any value name of {@link OrderStatus} (e.g 'ACTIVE')
     */
    void setStatus(Long orderId, String status);

    /**
     * Creates a new order from user's current cart and all of its products and saves it to database.
     * Doesn't clear the cart.
     * @return newly created order
     */
    Order createOrderFromCurrentUserCart(User user);

    void clearUserCart(User user);
}
