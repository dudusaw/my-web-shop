package com.example.mywebshop.repository;

import com.example.mywebshop.entity.Order;
import com.example.mywebshop.service.impl.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByStatusOrderByTimestampDesc(OrderStatus status);
}
