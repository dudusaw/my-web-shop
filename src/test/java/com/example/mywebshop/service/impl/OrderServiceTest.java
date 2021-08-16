package com.example.mywebshop.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mywebshop.config.exception.NotFoundException;
import com.example.mywebshop.repository.CartProductRepository;
import com.example.mywebshop.repository.OrderRepository;
import com.example.mywebshop.service.IMailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(SpringExtension.class)
public class OrderServiceTest {
    @MockBean
    private CartProductRepository cartProductRepository;

    @MockBean
    private IMailService iMailService;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Test
    public void testSetStatus() {
        // Arrange
        when(this.orderRepository.findById((Long) any())).thenThrow(new NotFoundException());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> this.orderService.setStatus(123L, "Status"));
        verify(this.orderRepository).findById((Long) any());
    }
}

