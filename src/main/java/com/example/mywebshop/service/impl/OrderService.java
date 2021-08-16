package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.NotFoundException;
import com.example.mywebshop.entity.CartProduct;
import com.example.mywebshop.entity.Order;
import com.example.mywebshop.entity.OrderProduct;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.repository.CartProductRepository;
import com.example.mywebshop.repository.OrderRepository;
import com.example.mywebshop.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
@Slf4j
public class OrderService implements com.example.mywebshop.service.IOrderService {

    private final OrderRepository orderRepository;
    private final CartProductRepository cartProductRepository;
    private final IMailService mailService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        CartProductRepository cartProductRepository,
                        IMailService mailService) {
        this.orderRepository = orderRepository;
        this.cartProductRepository = cartProductRepository;
        this.mailService = mailService;
    }

    @Override
    public void setStatus(Long orderId, String status) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(NotFoundException::new);
        OrderStatus newOrderStatus = OrderStatus.valueOf(status);
        order.setStatus(newOrderStatus);
        orderRepository.save(order);
    }

    @Override
    public Order createOrderFromCurrentUserCart(User user) {
        if (user.getCartProducts().isEmpty()) {
            return null;
        }
        Order order = new Order();
        order.setUser(user);
        order.setProducts(new ArrayList<>());
        BigDecimal totalPrice = addOrderProductsAndCalculateTotalPrice(user, order);
        order.setTotalPrice(totalPrice);
        mailService.sendOrderFormedMessage(user, true);
        log.info("user {} successfully formed an order: {}", user.getUsername(), order);
        return orderRepository.save(order);
    }

    private BigDecimal addOrderProductsAndCalculateTotalPrice(User user, Order order) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartProduct cartProduct : user.getCartProducts().values()) {
            OrderProduct orderProduct = new OrderProduct(
                    order,
                    cartProduct.getProduct(),
                    cartProduct.getCount());
            order.getProducts().add(orderProduct);
            totalPrice = totalPrice.add(cartProduct.getProduct().getPrice());
        }
        return totalPrice;
    }

    @Override
    public void clearUserCart(User user) {
        Collection<CartProduct> cartProducts = user.getCartProducts().values();
        cartProductRepository.deleteAllInBatch(cartProducts);
        user.getCartProducts().clear();
    }
}
