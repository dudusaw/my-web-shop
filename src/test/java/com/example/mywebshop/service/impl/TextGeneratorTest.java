package com.example.mywebshop.service.impl;

import com.example.mywebshop.service.ITextGenerator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class TextGeneratorTest {

    private final ITextGenerator textGenerator;

    @Autowired
    TextGeneratorTest(ITextGenerator textGenerator) {
        this.textGenerator = textGenerator;
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8})
    void notNullResult_test(int numParagraphs) {
        String result = textGenerator.generateText(numParagraphs, TextGenLength.SHORT);

        log.info(result);
        Assertions
                .assertThat(result)
                .isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8})
    void differentCallsReturnUniqueText_test(int numParagraphs) {
        String result1 = textGenerator.generateText(numParagraphs, TextGenLength.SHORT);
        String result2 = textGenerator.generateText(numParagraphs, TextGenLength.SHORT);
        String result3 = textGenerator.generateText(numParagraphs, TextGenLength.SHORT);

        Assertions
                .assertThat(result1)
                .isNotEqualTo(result2)
                .isNotEqualTo(result3);
    }
}