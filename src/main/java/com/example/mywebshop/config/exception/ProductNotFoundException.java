package com.example.mywebshop.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such product")
public class ProductNotFoundException extends RuntimeException {
}
