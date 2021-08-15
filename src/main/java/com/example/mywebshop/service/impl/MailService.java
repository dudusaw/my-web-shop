package com.example.mywebshop.service.impl;

import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailService implements IMailService {

    private final JavaMailSender mailSender;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOrderFormedMessage(User user, boolean async) {
        Runnable runnable = () -> {
            SimpleMailMessage message = new SimpleMailMessage();
            String text = String.format("Hello %s!\nYour order has successfully formed. Have a good day!", user.getUsername());
            message.setTo(user.getEmail());
            message.setSubject("my-web-shop order");
            message.setText(text);
            mailSender.send(message);
        };
        if (async) {
            executorService.submit(runnable);
        } else {
            runnable.run();
        }
    }

}
