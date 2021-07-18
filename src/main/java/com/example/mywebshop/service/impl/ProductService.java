package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.NotFoundException;
import com.example.mywebshop.config.validation.ValidProduct;
import com.example.mywebshop.config.validation.ValidReview;
import com.example.mywebshop.entity.*;
import com.example.mywebshop.repository.ProductMajorCategoryRepository;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.repository.ProductReviewRepository;
import com.example.mywebshop.repository.ReviewVoteRepository;
import com.example.mywebshop.service.IFileService;
import com.example.mywebshop.service.IProductService;
import com.example.mywebshop.service.ITextGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService implements IProductService {

    @Value("${my-values.shortDescriptionMaxSymbols}")
    public int shortDescriptionMaxSymbols;
    @Value("${my-values.longDescriptionMaxSymbols}")
    public int longDescriptionMaxSymbols;

    private final ReviewVoteRepository reviewVoteRepository;
    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final ProductMajorCategoryRepository majorCategoryRepository;
    private final ITextGenerator textGenerator;
    private final IFileService fileService;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductMajorCategoryRepository majorCategoryRepository,
                          ITextGenerator textGenerator,
                          IFileService fileService,
                          ReviewVoteRepository reviewVoteRepository,
                          ProductReviewRepository productReviewRepository) {
        this.productRepository = productRepository;
        this.majorCategoryRepository = majorCategoryRepository;
        this.textGenerator = textGenerator;
        this.fileService = fileService;
        this.reviewVoteRepository = reviewVoteRepository;
        this.productReviewRepository = productReviewRepository;
    }


    @Override
    public Long addNewProduct(ValidProduct validProduct) {
        ProductMajorCategory category = majorCategoryRepository
                .findByName(validProduct.getCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));

        Product product = new Product();
        product.setTitle(validProduct.getTitle());
        product.setShortDescription(validProduct.getShortDescription());
        product.setDescription(validProduct.getDescription());
        product.setPrice(validProduct.getPrice());
        product.setCategory(category);
        FileMeta fileMeta = fileService.saveImageFileIfExists(validProduct.getImageFile());
        if (fileMeta != null) {
            product.setImageFiles(List.of(fileMeta));
        }
        return productRepository.save(product).getId();
    }

    @Override
    public List<String> getMajorCategoriesList() {
        return majorCategoryRepository
                .findAll()
                .stream()
                .map(ProductMajorCategory::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void submitReview(Long productId, User user, ValidReview validReview, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return;

        Product product = productRepository
                .findById(productId)
                .orElseThrow(NotFoundException::new);
        Optional<ProductReview> persistedReview = productReviewRepository.findByUserIdAndProductId(user.getId(), productId);
        if (persistedReview.isPresent()) {
            bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "review already exists by this user"));
            return;
        }
        ProductReview newReview = new ProductReview(user, product, validReview.getRating(), validReview.getReview());
        productReviewRepository.save(newReview);
    }

    @Override
    public void deleteReview(Long reviewId, User user) {
        ProductReview productReview = productReviewRepository
                .findById(reviewId)
                .orElseThrow(NotFoundException::new);

        if (user.hasRole("ADMIN") || productReview
                .getUser()
                .getUsername()
                .equals(user.getUsername())) {
            productReviewRepository.delete(productReview);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "no rights for that");
        }
    }

    @Override
    public void submitReviewVote(Long reviewId, User user, boolean positive) {
        ProductReview productReview = productReviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no such review " + reviewId));
        Optional<ProductReviewVote> persistedVote = reviewVoteRepository.findByUserIdAndReviewId(user.getId(), productReview.getId());
        if (persistedVote.isPresent()) {
            ProductReviewVote vote = persistedVote.get();
            if (vote.isPositive() == positive) {
                reviewVoteRepository.delete(vote);
            } else {
                vote.setPositive(!vote.isPositive());
            }
        } else {
            ProductReviewVote vote = new ProductReviewVote(user, productReview, positive);
            reviewVoteRepository.save(vote);
        }
    }

    @Override
    public void updateProductRatingFromReviews(Long id) {
        Product product = getByIdOrThrow(id);
        int reviewCount = product.getReviews().size();
        double ratingSum = product
                .getReviews()
                .stream()
                .mapToInt(ProductReview::getRating)
                .sum();
        product.setRating(ratingSum / reviewCount);
        productRepository.save(product);
    }

    @Override
    public void initProductReviewVotes(Product product) {
        for (ProductReview review : product.getReviews()) {
            int allVotesCount = review.getVotes().size();
            int positiveVoteCount = productRepository.getPositiveVoteCount(review.getId());
            int negativeVoteCount = allVotesCount - positiveVoteCount;
            review.setPositiveVoteCount(positiveVoteCount);
            review.setNegativeVoteCount(negativeVoteCount);
        }
    }

    @Override
    public Product getByIdOrThrow(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void createRandomProducts(int num) {
        long[] categoryIds = majorCategoryRepository.findAll().stream().mapToLong(ProductMajorCategory::getId).toArray();
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            long categoryId = categoryIds[RandomUtils.nextInt(0, categoryIds.length)];
            ProductMajorCategory category = majorCategoryRepository.findById(categoryId).orElseThrow();
            Product newProduct = generateProduct(i, category);
            productList.add(newProduct);
        }
        productRepository.saveAll(productList);
    }

    private Product generateProduct(int i, ProductMajorCategory category) {
        String title = "Product " + i + RandomStringUtils.randomAlphabetic(2, 2);
        String rndShortDescription = textGenerator.generateText(1, TextGenLength.SHORT);
        String rndLongDescription = textGenerator.generateText(3, TextGenLength.LONG);
        rndShortDescription =
                rndShortDescription.substring(0, Math.min(rndShortDescription.length(), shortDescriptionMaxSymbols)).trim();
        rndLongDescription =
                rndLongDescription.substring(0, Math.min(rndLongDescription.length(), longDescriptionMaxSymbols)).trim();
        double rating = RandomUtils.nextDouble(1, 5);
        BigDecimal price = BigDecimal.valueOf(RandomUtils.nextDouble(1, 500));
        rating = BigDecimal.valueOf(rating).setScale(1, RoundingMode.HALF_UP).doubleValue();
        price = price.setScale(2, RoundingMode.HALF_UP);
        return new Product(title, rndShortDescription, rndLongDescription, rating, price, category);
    }

    @Override
    public Page<Product> findAllOnPage(int page, int pageSize) {
        return productRepository.findAll(Pageable.ofSize(pageSize).withPage(page));
    }
}
