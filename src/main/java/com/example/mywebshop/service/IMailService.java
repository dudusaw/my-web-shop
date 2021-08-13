package com.example.mywebshop.service;

import com.example.mywebshop.entity.User;

public interface IMailService {
    void sendOrderFormedMessage(User user);
}
