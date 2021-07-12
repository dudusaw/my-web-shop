package com.example.mywebshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@PropertySource(value = {"classpath:my-example.properties"})
public class MyWebShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWebShopApplication.class, args);
    }

}
