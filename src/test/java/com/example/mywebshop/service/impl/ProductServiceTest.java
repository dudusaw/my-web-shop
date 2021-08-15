package com.example.mywebshop.service.impl;

import com.example.mywebshop.dto.ValidProduct;
import com.example.mywebshop.entity.CartProduct;
import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductMajorCategory;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.entity.ProductReviewVote;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.repository.FileMetaRepository;
import com.example.mywebshop.repository.ProductMajorCategoryRepository;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.service.IProductService;
import com.example.mywebshop.service.ITextGenerator;

import java.time.LocalDateTime;

import java.util.HashMap;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestEntityManager
@Transactional
class ProductServiceTest {

    private final IProductService productService;
    private final ProductRepository productRepository;
    private final TestEntityManager entityManager;

    @Test
    public void testConstructor() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ProductMajorCategoryRepository majorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService actualProductService = new ProductService(productRepository, majorCategoryRepository, textGenerator,
                fileService, new FileCompressor());

        assertTrue(actualProductService.getMajorCategoriesList().isEmpty());
        assertEquals(0, actualProductService.shortDescriptionMaxSymbols);
        assertEquals(0, actualProductService.longDescriptionMaxSymbols);
    }

    @Autowired
    ProductServiceTest(IProductService productService, ProductRepository productRepository, TestEntityManager entityManager) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @Test
    void addNewProductTest_valid() {
        ValidProduct validProduct = new ValidProduct();
        String title = "test_product";
        String desc = "asd";
        String shortDesc = "asd";
        String category = "home"; // this from migrations
        BigDecimal price = BigDecimal.valueOf(352);
        validProduct.setTitle(title);
        validProduct.setCategory(category);
        validProduct.setPrice(price);
        validProduct.setDescription(desc);
        validProduct.setShortDescription(shortDesc);

        Long id = productService.addNewProduct(validProduct);

        Optional<Product> product = productRepository.findById(id);
        assertThat(product.isPresent()).isTrue();
        assertThat(product.get().getTitle()).isEqualTo(title);
        assertThat(product.get().getPrice()).isEqualTo(price);
    }

    @Test
    public void testAddNewProduct() {
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
        ArrayList<FileMeta> fileMetaList = new ArrayList<>();
        product.setImageFiles(fileMetaList);
        product.setTitle("Dr");
        product.setDescription("The characteristics of someone or something");
        product.setCartProducts(new ArrayList<>());
        product.setRating(10.0);
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.save(any())).thenReturn(product);

        ProductMajorCategory productMajorCategory1 = new ProductMajorCategory();
        productMajorCategory1.setId(123L);
        productMajorCategory1.setName("Name");
        productMajorCategory1.setProducts(new HashMap<>(1));
        ProductMajorCategoryRepository productMajorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        when(productMajorCategoryRepository.findByName(anyString()))
                .thenReturn(Optional.of(productMajorCategory1));
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService productService = new ProductService(productRepository, productMajorCategoryRepository, textGenerator,
                fileService, new FileCompressor());

        ValidProduct validProduct = new ValidProduct();
        validProduct.setShortDescription("Short Description");
        validProduct.setCategory("Category");
        validProduct.setPrice(BigDecimal.valueOf(42L));
        validProduct.setTitle("Dr");
        validProduct.setImageFiles(new ArrayList<>());
        validProduct.setDescription("The characteristics of someone or something");
        assertEquals(123L, productService.addNewProduct(validProduct).longValue());
        verify(productRepository).save(any());
        verify(productMajorCategoryRepository).findByName(anyString());
        assertEquals(fileMetaList, productService.getMajorCategoriesList());
    }

    @Test
    public void testAddNewProduct2() {
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
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.save(any())).thenReturn(product);
        ProductMajorCategoryRepository productMajorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        when(productMajorCategoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService productService = new ProductService(productRepository, productMajorCategoryRepository, textGenerator,
                fileService, new FileCompressor());

        ValidProduct validProduct = new ValidProduct();
        validProduct.setShortDescription("Short Description");
        validProduct.setCategory("Category");
        validProduct.setPrice(BigDecimal.valueOf(42L));
        validProduct.setTitle("Dr");
        validProduct.setImageFiles(new ArrayList<>());
        validProduct.setDescription("The characteristics of someone or something");
        assertThrows(ResponseStatusException.class, () -> productService.addNewProduct(validProduct));
        verify(productMajorCategoryRepository).findByName(anyString());
    }

    @Test
    public void testGetMajorCategoriesList() {
        ProductMajorCategoryRepository productMajorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        when(productMajorCategoryRepository.findAll()).thenReturn(new ArrayList<>());
        ProductRepository productRepository = mock(ProductRepository.class);
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        assertTrue((new ProductService(productRepository, productMajorCategoryRepository, textGenerator, fileService,
                new FileCompressor())).getMajorCategoriesList().isEmpty());
        verify(productMajorCategoryRepository).findAll();
    }

    @RepeatedTest(5)
    void initProductReviewVotesTest() {
        Product product = new Product();
        User user = new User();
        ProductReview review = new ProductReview(user, product, 5, "");
        product.setReviews(List.of(review));
        int totalVotes = 100;
        int positiveVotes = 0;
        product = entityManager.persist(product);
        user = entityManager.persist(user);
        review = entityManager.persist(review);
        List<ProductReviewVote> votes = new ArrayList<>();
        for (int j = 0; j < totalVotes; j++) {
            boolean positive = RandomUtils.nextBoolean();
            positiveVotes += positive ? 1 : 0;
            ProductReviewVote vote = new ProductReviewVote(user, review, positive);
            votes.add(vote);
            entityManager.persist(vote);
        }
        review.setVotes(votes);
        entityManager.flush();
        product = entityManager.find(Product.class, product.getId());

        productService.initProductReviewVotes(product);

        review = entityManager.find(ProductReview.class, review.getId());

        assertThat(review.getPositiveVoteCount()).isEqualTo(positiveVotes);
        assertThat(review.getNegativeVoteCount()).isEqualTo(totalVotes - positiveVotes);
    }

    @Test
    public void testInitProductReviewVotes2() {
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.getPositiveVoteCount(any())).thenReturn(3);
        ProductMajorCategoryRepository majorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService productService = new ProductService(productRepository, majorCategoryRepository, textGenerator,
                fileService, new FileCompressor());

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
        ArrayList<CartProduct> cartProductList = new ArrayList<>();
        product.setCartProducts(cartProductList);
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
        productReview.setRating(0);

        ArrayList<ProductReview> productReviewList = new ArrayList<>();
        productReviewList.add(productReview);

        ProductMajorCategory productMajorCategory1 = new ProductMajorCategory();
        productMajorCategory1.setId(123L);
        productMajorCategory1.setName("Name");
        productMajorCategory1.setProducts(new HashMap<>(1));

        Product product1 = new Product();
        product1.setReviews(productReviewList);
        product1.setShortDescription("Short Description");
        product1.setId(123L);
        product1.setCategory(productMajorCategory1);
        product1.setPrice(BigDecimal.valueOf(42L));
        product1.setImageFiles(new ArrayList<>());
        product1.setTitle("Dr");
        product1.setDescription("The characteristics of someone or something");
        product1.setCartProducts(new ArrayList<>());
        product1.setRating(10.0);
        productService.initProductReviewVotes(product1);
        verify(productRepository).getPositiveVoteCount(any());
        assertEquals(cartProductList, productService.getMajorCategoriesList());
    }

    @Test
    public void testUpdateProductRatingFromReviews() {
        ProductMajorCategory productMajorCategory = new ProductMajorCategory();
        productMajorCategory.setId(123L);
        productMajorCategory.setName("Name");
        productMajorCategory.setProducts(new HashMap<>(1));

        Product product = new Product();
        ArrayList<ProductReview> productReviewList = new ArrayList<>();
        product.setReviews(productReviewList);
        product.setShortDescription("Short Description");
        product.setId(123L);
        product.setCategory(productMajorCategory);
        product.setPrice(BigDecimal.valueOf(42L));
        product.setImageFiles(new ArrayList<>());
        product.setTitle("Dr");
        product.setDescription("The characteristics of someone or something");
        product.setCartProducts(new ArrayList<>());
        product.setRating(10.0);
        Optional<Product> ofResult = Optional.of(product);

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
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.save(any())).thenReturn(product1);
        when(productRepository.findById(any())).thenReturn(ofResult);
        ProductMajorCategoryRepository majorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService productService = new ProductService(productRepository, majorCategoryRepository, textGenerator,
                fileService, new FileCompressor());
        productService.updateProductRatingFromReviews(123L);
        verify(productRepository).findById(any());
        verify(productRepository).save(any());
        assertEquals(productReviewList, productService.getMajorCategoriesList());
    }

    @Test
    public void testUpdateProductRatingFromReviews2() {
        // Arrange
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
        product.setCharacteristics("Characteristics");
        product.setRating(10.0);
        Optional<Product> ofResult = Optional.of(product);

        ProductMajorCategory productMajorCategory1 = new ProductMajorCategory();
        productMajorCategory1.setId(123L);
        productMajorCategory1.setName("Name");
        productMajorCategory1.setProducts(new HashMap<>(1));

        Product product1 = new Product();
        ArrayList<ProductReview> productReviewList = new ArrayList<>();
        product1.setReviews(productReviewList);
        product1.setShortDescription("Short Description");
        product1.setId(123L);
        product1.setCategory(productMajorCategory1);
        product1.setPrice(BigDecimal.valueOf(42L));
        product1.setImageFiles(new ArrayList<>());
        product1.setTitle("Dr");
        product1.setDescription("The characteristics of someone or something");
        product1.setCartProducts(new ArrayList<>());
        product1.setCharacteristics("Characteristics");
        product1.setRating(10.0);
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.save(any())).thenReturn(product1);
        when(productRepository.findById(any())).thenReturn(ofResult);
        ProductMajorCategoryRepository majorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService productService = new ProductService(productRepository, majorCategoryRepository, textGenerator,
                fileService, new FileCompressor());

        // Act
        productService.updateProductRatingFromReviews(123L);

        // Assert
        verify(productRepository).findById(any());
        verify(productRepository).save(any());
        assertEquals(productReviewList, productService.getMajorCategoriesList());
    }

    @RepeatedTest(5)
    void updateProductRatingFromReviewsTest() {
        Product product = productRepository.getById(1L); // from migrations
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("u" + i);
            ProductReview productReview = new ProductReview(user, product, RandomUtils.nextInt(1, 5), "");
            entityManager.persist(user);
            entityManager.persist(productReview);
        }
        entityManager.flush();
        product = entityManager.refresh(product);

        int count = product.getReviews().size();
        double ratingSum = product.getReviews().stream().mapToInt(ProductReview::getRating).sum();
        double resultRating = ratingSum / count;

        productService.updateProductRatingFromReviews(product.getId());

        assertThat(product.getRating()).isEqualTo(resultRating);
    }

    @Test
    void deleteProductTest_noSuchProductAfterDelete() {
        Product product = new Product();
        product.setTitle("asd");

        Product savedProduct = productRepository.save(product);

        productService.deleteProduct(savedProduct.getId());

        assertThat(productRepository.findById(savedProduct.getId()).isEmpty()).isTrue();
    }

    @Test
    public void testDeleteProduct() {
        ProductRepository productRepository = mock(ProductRepository.class);
        doNothing().when(productRepository).deleteById(any());
        ProductMajorCategoryRepository majorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService productService = new ProductService(productRepository, majorCategoryRepository, textGenerator,
                fileService, new FileCompressor());
        productService.deleteProduct(123L);
        verify(productRepository).deleteById(any());
        assertTrue(productService.getMajorCategoriesList().isEmpty());
    }

    @Test
    void getByIdOrThrowTest_throwsIfNotExists() {
        assertThatThrownBy(() -> productService.getByIdOrThrow(5623675982L));
    }

    @Test
    public void testGetByIdOrThrow() {
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
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        ProductMajorCategoryRepository majorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService productService = new ProductService(productRepository, majorCategoryRepository, textGenerator,
                fileService, new FileCompressor());
        Product actualByIdOrThrow = productService.getByIdOrThrow(123L);
        assertSame(product, actualByIdOrThrow);
        assertEquals("42", actualByIdOrThrow.getPrice().toString());
        verify(productRepository).findById(any());
        List<FileMeta> expectedMajorCategoriesList = actualByIdOrThrow.getImageFiles();
        assertEquals(expectedMajorCategoriesList, productService.getMajorCategoriesList());
    }

    @Test
    public void testCreateRandomProducts() {
        ProductMajorCategoryRepository productMajorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        when(productMajorCategoryRepository.findAll()).thenReturn(new ArrayList<>());
        ProductRepository productRepository = mock(ProductRepository.class);
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> (new ProductService(productRepository,
                productMajorCategoryRepository, textGenerator, fileService, new FileCompressor())).createRandomProducts(10));
        verify(productMajorCategoryRepository).findAll();
    }

    @Test
    public void testCreateRandomProducts2() {
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.saveAll(any())).thenReturn(new ArrayList<>());

        ProductMajorCategory productMajorCategory = new ProductMajorCategory();
        productMajorCategory.setId(123L);
        productMajorCategory.setName("Name");
        productMajorCategory.setProducts(new HashMap<>(1));

        ArrayList<ProductMajorCategory> productMajorCategoryList = new ArrayList<>();
        productMajorCategoryList.add(productMajorCategory);

        ProductMajorCategory productMajorCategory1 = new ProductMajorCategory();
        productMajorCategory1.setId(123L);
        productMajorCategory1.setName("Name");
        productMajorCategory1.setProducts(new HashMap<>(1));
        ProductMajorCategoryRepository productMajorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        when(productMajorCategoryRepository.findById(any()))
                .thenReturn(Optional.of(productMajorCategory1));
        when(productMajorCategoryRepository.findAll()).thenReturn(productMajorCategoryList);
        ITextGenerator iTextGenerator = mock(ITextGenerator.class);
        when(iTextGenerator.generateText(anyInt(), any())).thenReturn("foo");
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService productService = new ProductService(productRepository, productMajorCategoryRepository,
                iTextGenerator, fileService, new FileCompressor());
        productService.createRandomProducts(10);
        verify(productRepository).saveAll(any());
        verify(productMajorCategoryRepository).findAll();
        verify(productMajorCategoryRepository, times(10)).findById(any());
        verify(iTextGenerator, times(20)).generateText(anyInt(), any());
        assertEquals(1, productService.getMajorCategoriesList().size());
    }

    @Test
    public void testFindAllOnPage() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ArrayList<Product> productList = new ArrayList<>();
        PageImpl<Product> pageImpl = new PageImpl<>(productList);
        when(productRepository.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(pageImpl);
        ProductMajorCategoryRepository majorCategoryRepository = mock(ProductMajorCategoryRepository.class);
        ITextGenerator textGenerator = mock(ITextGenerator.class);
        LocalFileStorage fileService = new LocalFileStorage(mock(FileMetaRepository.class));
        ProductService productService = new ProductService(productRepository, majorCategoryRepository, textGenerator,
                fileService, new FileCompressor());
        Page<Product> actualFindAllOnPageResult = productService.findAllOnPage(1, 3);
        assertSame(pageImpl, actualFindAllOnPageResult);
        assertTrue(actualFindAllOnPageResult.toList().isEmpty());
        verify(productRepository).findAll((org.springframework.data.domain.Pageable) any());
        assertEquals(productList, productService.getMajorCategoriesList());
    }
}