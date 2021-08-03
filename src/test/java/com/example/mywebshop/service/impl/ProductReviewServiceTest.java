package com.example.mywebshop.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mywebshop.dto.ValidReview;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductMajorCategory;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.entity.ProductReviewVote;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.entity.UserRole;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.repository.ProductReviewRepository;
import com.example.mywebshop.repository.ReviewVoteRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindException;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {ProductReviewService.class})
@ExtendWith(SpringExtension.class)
public class ProductReviewServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductReviewService productReviewService;

    @MockBean
    private ReviewVoteRepository reviewVoteRepository;

    @Test
    public void testGetReviewById() {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview = new ProductReview();
        productReview.setUser(user);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);
        Optional<ProductReview> ofResult = Optional.of(productReview);
        when(this.productReviewRepository.findById(any())).thenReturn(ofResult);

        // Act
        ProductReview actualReviewById = this.productReviewService.getReviewById(123L);

        // Assert
        assertSame(productReview, actualReviewById);
        assertEquals("42", actualReviewById.getProduct().getPrice().toString());
        verify(this.productReviewRepository).findById(any());
    }

    @Test
    public void testDeleteReview() {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview = new ProductReview();
        productReview.setUser(user);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);
        Optional<ProductReview> ofResult = Optional.of(productReview);
        doNothing().when(this.productReviewRepository).delete(any());
        when(this.productReviewRepository.findById(any())).thenReturn(ofResult);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

        // Act
        this.productReviewService.deleteReview(123L, user1);

        // Assert
        verify(this.productReviewRepository).delete(any());
        verify(this.productReviewRepository).findById(any());
    }

    @Test
    public void testDeleteReview2() {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("ADMIN");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview = new ProductReview();
        productReview.setUser(user);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);
        Optional<ProductReview> ofResult = Optional.of(productReview);
        doNothing().when(this.productReviewRepository).delete(any());
        when(this.productReviewRepository.findById(any())).thenReturn(ofResult);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> this.productReviewService.deleteReview(123L, user1));
        verify(this.productReviewRepository).findById(any());
    }

    @Test
    public void testDeleteReview4() {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview = new ProductReview();
        productReview.setUser(user);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);
        Optional<ProductReview> ofResult = Optional.of(productReview);
        doNothing().when(this.productReviewRepository).delete(any());
        when(this.productReviewRepository.findById(any())).thenReturn(ofResult);

        UserRole userRole = new UserRole();
        userRole.setId(123L);
        userRole.setName("ADMIN");

        ArrayList<UserRole> userRoleList = new ArrayList<>();
        userRoleList.add(userRole);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(userRoleList);
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

        // Act
        this.productReviewService.deleteReview(123L, user1);

        // Assert
        verify(this.productReviewRepository).delete(any());
        verify(this.productReviewRepository).findById(any());
    }

    @Test
    public void testCheckAndSubmitReview() {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview = new ProductReview();
        productReview.setUser(user);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);
        Optional<ProductReview> ofResult = Optional.of(productReview);
        when(this.productReviewRepository.findByUserIdAndProductId(any(), any())).thenReturn(ofResult);

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
        Optional<Product> ofResult1 = Optional.of(product1);
        when(this.productRepository.findById(any())).thenReturn(ofResult1);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

        ValidReview validReview = new ValidReview();
        validReview.setReview("Review");
        validReview.setRating(1);
        BindException bindException = new BindException(
                new BindException(new BindException(new BindException(new BindException("Target", "Object Name")))));

        // Act
        this.productReviewService.checkAndSubmitReview(123L, user1, validReview, bindException);

        // Assert
        verify(this.productReviewRepository).findByUserIdAndProductId(any(), any());
        verify(this.productRepository).findById(any());
        assertEquals("org.springframework.validation.BindException: org.springframework.validation.BindException:"
                + " org.springframework.validation.BindException: org.springframework.validation.BindException:"
                + " org.springframework.validation.BindException: org.springframework.validation.BeanPropertyBindingResult:"
                + " 1 errors\n"
                + "Error in object 'Object Name': codes []; arguments []; default message [review already exists by"
                + " this user]", bindException.toString());
        assertTrue(bindException.hasErrors());
    }

    @Test
    public void testCheckAndSubmitReview2() {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview = new ProductReview();
        productReview.setUser(user);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);
        when(this.productReviewRepository.save(any())).thenReturn(productReview);
        when(this.productReviewRepository.findByUserIdAndProductId(any(), any()))
                .thenReturn(Optional.empty());

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
        Optional<Product> ofResult = Optional.of(product1);
        when(this.productRepository.findById(any())).thenReturn(ofResult);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

        ValidReview validReview = new ValidReview();
        validReview.setReview("Review");
        validReview.setRating(1);

        // Act
        this.productReviewService.checkAndSubmitReview(123L, user1, validReview, new BindException(
                new BindException(new BindException(new BindException(new BindException("Target", "Object Name"))))));

        // Assert
        verify(this.productReviewRepository).findByUserIdAndProductId(any(), any());
        verify(this.productReviewRepository).save(any());
        verify(this.productRepository).findById(any());
    }

    @Test
    public void testSubmitReviewVote() {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview = new ProductReview();
        productReview.setUser(user1);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);

        ProductReviewVote productReviewVote = new ProductReviewVote();
        productReviewVote.setPositive(true);
        productReviewVote.setUser(user);
        productReviewVote.setId(123L);
        productReviewVote.setReview(productReview);
        Optional<ProductReviewVote> ofResult = Optional.of(productReviewVote);
        doNothing().when(this.reviewVoteRepository).delete(any());
        when(this.reviewVoteRepository.findByUserIdAndReviewId(any(), any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setPassword("iloveyou");
        user2.setRoles(new ArrayList<>());
        user2.setUsername("janedoe");
        user2.setId(123L);
        user2.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview1 = new ProductReview();
        productReview1.setUser(user2);
        productReview1.setReview("Review");
        productReview1.setPositiveVoteCount(3);
        productReview1.setProduct(product1);
        productReview1.setVotes(new ArrayList<>());
        productReview1.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview1.setId(123L);
        productReview1.setNegativeVoteCount(3);
        productReview1.setRating(1);
        Optional<ProductReview> ofResult1 = Optional.of(productReview1);
        when(this.productReviewRepository.findById(any())).thenReturn(ofResult1);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setPassword("iloveyou");
        user3.setRoles(new ArrayList<>());
        user3.setUsername("janedoe");
        user3.setId(123L);
        user3.setCartProducts(new HashMap<>(1));

        // Act
        this.productReviewService.submitReviewVote(123L, user3, true);

        // Assert
        verify(this.reviewVoteRepository).delete(any());
        verify(this.reviewVoteRepository).findByUserIdAndReviewId(any(), any());
        verify(this.productReviewRepository).findById(any());
    }

    @Test
    public void testSubmitReviewVote2() {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview = new ProductReview();
        productReview.setUser(user1);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);

        ProductReviewVote productReviewVote = new ProductReviewVote();
        productReviewVote.setPositive(false);
        productReviewVote.setUser(user);
        productReviewVote.setId(123L);
        productReviewVote.setReview(productReview);
        Optional<ProductReviewVote> ofResult = Optional.of(productReviewVote);
        doNothing().when(this.reviewVoteRepository).delete(any());
        when(this.reviewVoteRepository.findByUserIdAndReviewId(any(), any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setPassword("iloveyou");
        user2.setRoles(new ArrayList<>());
        user2.setUsername("janedoe");
        user2.setId(123L);
        user2.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview1 = new ProductReview();
        productReview1.setUser(user2);
        productReview1.setReview("Review");
        productReview1.setPositiveVoteCount(3);
        productReview1.setProduct(product1);
        productReview1.setVotes(new ArrayList<>());
        productReview1.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview1.setId(123L);
        productReview1.setNegativeVoteCount(3);
        productReview1.setRating(1);
        Optional<ProductReview> ofResult1 = Optional.of(productReview1);
        when(this.productReviewRepository.findById(any())).thenReturn(ofResult1);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setPassword("iloveyou");
        user3.setRoles(new ArrayList<>());
        user3.setUsername("janedoe");
        user3.setId(123L);
        user3.setCartProducts(new HashMap<>(1));

        // Act
        this.productReviewService.submitReviewVote(123L, user3, true);

        // Assert
        verify(this.reviewVoteRepository).findByUserIdAndReviewId(any(), any());
        verify(this.productReviewRepository).findById(any());
    }

    @Test
    public void testSubmitReviewVote3() {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview = new ProductReview();
        productReview.setUser(user1);
        productReview.setReview("Review");
        productReview.setPositiveVoteCount(3);
        productReview.setProduct(product);
        productReview.setVotes(new ArrayList<>());
        productReview.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview.setId(123L);
        productReview.setNegativeVoteCount(3);
        productReview.setRating(1);

        ProductReviewVote productReviewVote = new ProductReviewVote();
        productReviewVote.setPositive(true);
        productReviewVote.setUser(user);
        productReviewVote.setId(123L);
        productReviewVote.setReview(productReview);
        when(this.reviewVoteRepository.save(any())).thenReturn(productReviewVote);
        doNothing().when(this.reviewVoteRepository).delete(any());
        when(this.reviewVoteRepository.findByUserIdAndReviewId(any(), any()))
                .thenReturn(Optional.empty());

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setPassword("iloveyou");
        user2.setRoles(new ArrayList<>());
        user2.setUsername("janedoe");
        user2.setId(123L);
        user2.setCartProducts(new HashMap<>(1));

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

        ProductReview productReview1 = new ProductReview();
        productReview1.setUser(user2);
        productReview1.setReview("Review");
        productReview1.setPositiveVoteCount(3);
        productReview1.setProduct(product1);
        productReview1.setVotes(new ArrayList<>());
        productReview1.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        productReview1.setId(123L);
        productReview1.setNegativeVoteCount(3);
        productReview1.setRating(1);
        Optional<ProductReview> ofResult = Optional.of(productReview1);
        when(this.productReviewRepository.findById(any())).thenReturn(ofResult);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setPassword("iloveyou");
        user3.setRoles(new ArrayList<>());
        user3.setUsername("janedoe");
        user3.setId(123L);
        user3.setCartProducts(new HashMap<>(1));

        // Act
        this.productReviewService.submitReviewVote(123L, user3, true);

        // Assert
        verify(this.reviewVoteRepository).findByUserIdAndReviewId(any(), any());
        verify(this.reviewVoteRepository).save(any());
        verify(this.productReviewRepository).findById(any());
    }
}

