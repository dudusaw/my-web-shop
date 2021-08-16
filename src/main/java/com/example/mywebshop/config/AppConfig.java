package com.example.mywebshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CacheControl myCacheControl() {
        return CacheControl
                .maxAge(365, TimeUnit.DAYS)
                .mustRevalidate();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/denied").setViewName("denied");
        registry.addViewController("/login").setViewName("login-form");
    }

    @Configuration
    @Profile("local-storage")
    public static class AppResourceConfig implements WebMvcConfigurer {

        @Value("${my-values.storage-location}")
        private String storageLocation;

        private final CacheControl cacheControl;

        @Autowired
        public AppResourceConfig(@Qualifier("myCacheControl") CacheControl cacheControl) {
            this.cacheControl = cacheControl;
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry
                    .addResourceHandler("/file/**")
                    .setCacheControl(cacheControl)
                    .addResourceLocations("file:"+storageLocation);
        }
    }
}
