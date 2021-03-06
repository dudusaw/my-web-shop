package com.example.mywebshop.controller;

import com.example.mywebshop.entity.Product;
import com.example.mywebshop.repository.FileMetaRepository;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    private final MockMvc mvc;
    private final ProductRepository productRepository;
    private final IProductService productService;
    private final FileMetaRepository fileRepository;

    @Autowired
    AdminControllerTest(MockMvc mvc,
                        ProductRepository productRepository,
                        IProductService productService,
                        FileMetaRepository fileRepository) {
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
    void addProductWithImage_valid(@Value("classpath:test/sofa.jpg") Resource image)
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