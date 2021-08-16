package com.example.mywebshop.service;

import com.example.mywebshop.entity.User;

public interface IMailService {

    /**
     * Send an email message to user's provided email address.
     * @param async true if wait for the response
     */
    void sendOrderFormedMessage(User user, boolean async);
}
