package com.example.mywebshop.service.impl;

import com.example.mywebshop.service.ITextGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TextGenerator implements ITextGenerator {

    private final RestTemplate restTemplate;

    @Autowired
    public TextGenerator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String generateText(int numParagraphs, TextGenLength length) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.loripsum.net/api/{num}/{length}/plaintext")
                .buildAndExpand(numParagraphs, length).toString();
        return restTemplate.getForObject(url, String.class);
    }
}
