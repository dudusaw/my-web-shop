package com.example.mywebshop;

import com.example.mywebshop.config.YamlPropertySourceFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.RepairResult;
import org.flywaydb.core.api.output.ValidateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@SpringBootApplication
@PropertySource(value = {"classpath:my-conf.yml"}, factory = YamlPropertySourceFactory.class)
public class MyWebShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWebShopApplication.class, args);
    }

}
