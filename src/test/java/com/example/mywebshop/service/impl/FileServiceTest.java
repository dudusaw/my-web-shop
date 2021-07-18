package com.example.mywebshop.service.impl;

import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.repository.FileStoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@MockBean(classes = {FileStoreRepository.class})
class FileServiceTest {

    @Test
    void saveImageFileIfExistsTest(@Value("classpath:test/sofa.jpg") Resource imageFile,
                                   @Autowired FileService fileService,
                                   @Value("${my-values.image-location}") String imageLocation)
            throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                imageFile.getFilename(),
                MediaType.IMAGE_JPEG_VALUE,
                imageFile.getInputStream());

        FileMeta fileMeta = fileService.saveImageFileIfExists(mockMultipartFile);

        Path path = Paths.get(imageLocation).resolve(fileMeta.getPath());
        Assertions.assertThat(Files.exists(path)).isTrue();

        Files.deleteIfExists(path);
    }
}