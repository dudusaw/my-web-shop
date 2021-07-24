package com.example.mywebshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:my-example.properties"})
public class MyWebShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWebShopApplication.class, args);
    }

}
