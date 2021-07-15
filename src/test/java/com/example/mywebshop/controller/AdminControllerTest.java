package com.example.mywebshop.controller;

import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.repository.FileStoreRepository;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.service.IFileService;
import com.example.mywebshop.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    private MockMvc mvc;
    private ProductRepository productRepository;
    private IProductService productService;
    private FileStoreRepository fileRepository;

    @Autowired
    AdminControllerTest(MockMvc mvc,
                        ProductRepository productRepository,
                        IProductService productService,
                        FileStoreRepository fileRepository) {
        this.mvc = mvc;
        this.productRepository = productRepository;
        this.productService = productService;
        this.fileRepository = fileRepository;
    }

    @Test
    void deleteProductTest() throws Exception {
        Product product = productRepository.getById(1L); // from migrations
        mvc.perform(get("/admin/delete").param("id", String.valueOf(1L)));

        assertThat(productRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    void addProduct_invalid_missingField() throws Exception {
        String title = "test_product";
        String shortDesc = "asdf";
        String category = "home";
        BigDecimal price = BigDecimal.valueOf(123);

        Map<String, Object> model = mvc.perform(multipart("/admin/add-product")
                .file(new MockMultipartFile("image", new byte[0]))
                .param("title", title)
                .param("shortDescription", shortDesc)
                .param("price", String.valueOf(price))
                .param("category", category))
                .andExpect(status().isOk())
                .andExpect(view().name("add-product-panel"))
                .andReturn().getModelAndView().getModel();

        boolean success = (boolean) model.get("success");
        BindingResult bindingResult = (BindingResult) model.get("bindingResult");

        assertThat(bindingResult.hasFieldErrors("description")).isTrue();
        assertThat(success).isFalse();
    }

    @Test
    void addProductWithImage_valid(@Value("classpath:test/sofa.jpg") Resource image,
                                   @Value("${my-values.image-location}") String imageLocation)
            throws Exception {
        String title = "test_product";
        String shortDesc = "asdf";
        String desc = "asdf";
        String category = "home";
        BigDecimal price = BigDecimal.valueOf(123);

        MockMultipartFile file = new MockMultipartFile(
                "image",
                image.getFilename(),
                MediaType.IMAGE_JPEG_VALUE,
                image.getInputStream());

        Map<String, Object> model = mvc.perform(multipart("/admin/add-product")
                .file(file)
                .param("title", title)
                .param("shortDescription", shortDesc)
                .param("description", desc)
                .param("price", String.valueOf(price))
                .param("category", category))
                .andExpect(status().isOk())
                .andExpect(view().name("add-product-panel"))
                .andReturn().getModelAndView().getModel();

        boolean success = (boolean) model.get("success");

        List<Product> byTitle = productRepository.findAllByTitle(title);

        assertThat(success).isTrue();
        assertThat(byTitle.isEmpty()).isFalse();

        List<FileMeta> allByOriginalFilename = fileRepository.findAllByOriginalFilename(image.getFilename());

        assertThat(allByOriginalFilename.isEmpty()).isFalse();

        Path path = Paths.get(imageLocation).resolve(allByOriginalFilename.get(0).getPath());
        assertThat(Files.exists(path)).isTrue();
        Files.deleteIfExists(path);
    }

    @Test
    void addProductPanelTest_returnViewAndCategoriesList() throws Exception {
        Map<String, Object> model = mvc
                .perform(get("/admin/add-product"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-product-panel")).andReturn().getModelAndView().getModel();
        List<String> categoryList = (List<String>) model.get("categoryList");

        assertThat(categoryList).isEqualTo(productService.getMajorCategoriesList());
    }
}