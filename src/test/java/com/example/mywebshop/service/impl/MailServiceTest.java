package com.example.mywebshop.service.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.example.mywebshop.entity.CartProduct;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.entity.UserRole;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MailService.class})
@ExtendWith(SpringExtension.class)
public class MailServiceTest {
    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MailService mailService;

    @Test
    public void testSendOrderFormedMessage() throws MailException {
        // Arrange
        doNothing().when(this.javaMailSender).send((org.springframework.mail.SimpleMailMessage) any());

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<UserRole>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<Long, CartProduct>(1));

        // Act
        this.mailService.sendOrderFormedMessage(user, true);

        // Assert
        verify(this.javaMailSender).send((org.springframework.mail.SimpleMailMessage) any());
    }
}

