package com.example.mywebshop.service.impl;

import com.example.mywebshop.repository.FileMetaRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@MockBean(classes = {FileMetaRepository.class})
class FileServiceTest {

//    @Test
//    void saveImageFileIfExistsTest(@Value("classpath:test/sofa.jpg") Resource imageFile,
//                                   @Autowired FileService fileService,
//                                   @Value("${my-values.image-location}") String imageLocation)
//            throws IOException {
//        MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                "image",
//                imageFile.getFilename(),
//                MediaType.IMAGE_JPEG_VALUE,
//                imageFile.getInputStream());
//
//        FileMeta fileMeta = fileService.saveImageFileIfExists(mockMultipartFile);
//
//        Path path = Paths.get(imageLocation).resolve(fileMeta.getPath());
//        Assertions.assertThat(Files.exists(path)).isTrue();
//
//        Files.deleteIfExists(path);
//    }
}