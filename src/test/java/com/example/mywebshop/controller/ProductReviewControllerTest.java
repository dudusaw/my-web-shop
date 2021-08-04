package com.example.mywebshop.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.mywebshop.dto.ValidReview;
import com.example.mywebshop.entity.CartProduct;
import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductMajorCategory;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.entity.ProductReviewVote;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.entity.UserRole;
import com.example.mywebshop.service.IProductReviewService;
import com.example.mywebshop.service.IProductService;
import com.example.mywebshop.service.IUserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ProductReviewController.class})
@ExtendWith(SpringExtension.class)
public class ProductReviewControllerTest {
    @MockBean
    private IProductReviewService iProductReviewService;

    @MockBean
    private IProductService iProductService;

    @MockBean
    private IUserService iUserService;

    @Autowired
    private ProductReviewController productReviewController;

    @Test
    public void testDeleteReview() throws Exception {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));
        when(this.iUserService.findByPrincipal(any())).thenReturn(user);
        doNothing().when(this.iProductReviewService).deleteReview(any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/act/delete-review/{productId}/{reviewId}", 123L, 123L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(this.productReviewController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/products/123"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products/123"));
    }

    @Test
    public void testReviewSubmitVote() throws Exception {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));
        when(this.iUserService.findByPrincipal(any())).thenReturn(user);

        ProductMajorCategory productMajorCategory = new ProductMajorCategory();
        productMajorCategory.setId(123L);
        productMajorCategory.setName("Name");
        productMajorCategory.setProducts(new HashMap<>(1));

        Product product = new Product();
        product.setReviews(new ArrayList<>());
        product.setShortDescription("Short Description");
        product.setId(123L);
        product.setCategory(productMajorCategory);
        product.setPrice(BigDecimal.valueOf(42L));
        product.setImageFiles(new ArrayList<>());
        product.setTitle("Dr");
        product.setDescription("The characteristics of someone or something");
        product.setCartProducts(new ArrayList<>());
        product.setRating(10.0);
        doNothing().when(this.iProductService).initProductReviewVotes(any());
        when(this.iProductService.getByIdOrThrow(any())).thenReturn(product);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

        ProductMajorCategory productMajorCategory1 = new ProductMajorCategory();
        productMajorCategory1.setId(123L);
        productMajorCategory1.setName("Name");
        productMajorCategory1.setProducts(new HashMap<>(1));

        Product product1 = new Product();
        product1.setReviews(new ArrayList<>());
        product1.setShortDescription("Short Description");
        product1.setId(123L);
        product1.setCategory(productMajorCategory1);
        product1.setPrice(BigDecimal.valueOf(42L));
        product1.setImageFiles(new ArrayList<>());
        product1.setTitle("Dr");
        product1.setDescription("The characteristics of someone or something");
        product1.setCartProducts(new ArrayList<>());
        product1.setRating(10.0);

        ProductReview productReview = new ProductReview();
        productReview.setUser(user1);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product1);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);
        when(this.iProductReviewService.getReviewById(any())).thenReturn(productReview);
        doNothing().when(this.iProductReviewService).submitReviewVote(any(), any(), anyBoolean());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/act/vote/{productId}/{reviewId}/{positive}", 123L, 123L, true);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(this.productReviewController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("{\"positiveCount\":3,\"negativeCount\":3}"));
    }

    @Test
    public void testReviewSubmitVote2() throws Exception {
        // Arrange
        User user = new User("jane.doe@example.org", "janedoe", "iloveyou");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));
        when(this.iUserService.findByPrincipal(any())).thenReturn(user);

        ProductMajorCategory productMajorCategory = new ProductMajorCategory();
        productMajorCategory.setId(123L);
        productMajorCategory.setName("Name");
        productMajorCategory.setProducts(new HashMap<>(1));

        Product product = new Product();
        product.setReviews(new ArrayList<>());
        product.setShortDescription("Short Description");
        product.setId(123L);
        product.setCategory(productMajorCategory);
        product.setPrice(BigDecimal.valueOf(42L));
        product.setImageFiles(new ArrayList<>());
        product.setTitle("Dr");
        product.setDescription("The characteristics of someone or something");
        product.setCartProducts(new ArrayList<>());
        product.setRating(10.0);
        doNothing().when(this.iProductService).initProductReviewVotes(any());
        when(this.iProductService.getByIdOrThrow(any())).thenReturn(product);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

        ProductMajorCategory productMajorCategory1 = new ProductMajorCategory();
        productMajorCategory1.setId(123L);
        productMajorCategory1.setName("Name");
        productMajorCategory1.setProducts(new HashMap<>(1));

        Product product1 = new Product();
        product1.setReviews(new ArrayList<>());
        product1.setShortDescription("Short Description");
        product1.setId(123L);
        product1.setCategory(productMajorCategory1);
        product1.setPrice(BigDecimal.valueOf(42L));
        product1.setImageFiles(new ArrayList<>());
        product1.setTitle("Dr");
        product1.setDescription("The characteristics of someone or something");
        product1.setCartProducts(new ArrayList<>());
        product1.setRating(10.0);

        ProductReview productReview = new ProductReview();
        productReview.setUser(user1);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product1);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);
        when(this.iProductReviewService.getReviewById(any())).thenReturn(productReview);
        doNothing().when(this.iProductReviewService).submitReviewVote(any(), any(), anyBoolean());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/act/vote/{productId}/{reviewId}/{positive}", 123L, 123L, true);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(this.productReviewController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("{\"positiveCount\":3,\"negativeCount\":3}"));
    }

    @Test
    public void testSubmitReview() throws Exception {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));
        when(this.iUserService.findByPrincipal(any())).thenReturn(user);
        doNothing().when(this.iProductReviewService)
                .checkAndSubmitReview(any(), any(), any(),
                        any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/products/act/submit-review/{productId}",
                123L);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("validReview", String.valueOf(new ValidReview()));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(this.productReviewController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"errorMessages\":[\"must not be null\",\"must not be null\"],\"submitSuccess\":\"false\"}"));
    }
}

