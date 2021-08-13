package com.example.mywebshop.service.impl;

import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService implements IMailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOrderFormedMessage(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("my-web-shop order");
        message.setText("Hello!\nYour order has successfully formed. Have a good day!");
        mailSender.send(message);
    }

}
