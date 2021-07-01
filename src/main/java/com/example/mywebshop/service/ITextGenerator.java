package com.example.mywebshop.service;

import com.example.mywebshop.service.impl.TextGenLength;

public interface ITextGenerator {
    String generateText(int numParagraphs, TextGenLength length);
}
