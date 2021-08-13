package com.example.mywebshop.service.impl;

import com.example.mywebshop.entity.CartProduct;
import com.example.mywebshop.entity.Order;
import com.example.mywebshop.entity.OrderProduct;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.repository.CartProductRepository;
import com.example.mywebshop.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
public class OrderService implements com.example.mywebshop.service.IOrderService {

    private final OrderRepository orderRepository;
    private final CartProductRepository cartProductRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        CartProductRepository cartProductRepository) {
        this.orderRepository = orderRepository;
        this.cartProductRepository = cartProductRepository;
    }

    @Override
    public Order createOrderFromCurrentUserCart(User user) {
        if (user.getCartProducts().isEmpty()) {
            return null;
        }
        Order order = new Order();
        order.setUser(user);
        order.setProducts(new ArrayList<>());
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartProduct cartProduct : user.getCartProducts().values()) {
            OrderProduct orderProduct = new OrderProduct(
                    order,
                    cartProduct.getProduct(),
                    cartProduct.getCount());
            order.getProducts().add(orderProduct);
            totalPrice = totalPrice.add(cartProduct.getProduct().getPrice());
        }
        order.setTotalPrice(totalPrice);

        log.info("user {} successfully formed an order: {}", user.getUsername(), order);
        return orderRepository.save(order);
    }

    @Override
    public void clearUserCart(User user) {
        Collection<CartProduct> cartProducts = user.getCartProducts().values();
        cartProductRepository.deleteAllInBatch(cartProducts);
        user.getCartProducts().clear();
    }
}
