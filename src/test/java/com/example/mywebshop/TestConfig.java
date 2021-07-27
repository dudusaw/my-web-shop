package com.example.mywebshop;

import com.example.mywebshop.service.IFileService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MockBean(classes = {IFileService.class})
public class TestConfig {
}
