package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.NotFoundException;
import com.example.mywebshop.dto.FileTransferInfo;
import com.example.mywebshop.dto.ValidProduct;
import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.ProductMajorCategory;
import com.example.mywebshop.entity.ProductReview;
import com.example.mywebshop.repository.ProductMajorCategoryRepository;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.repository.ProductReviewRepository;
import com.example.mywebshop.repository.ReviewVoteRepository;
import com.example.mywebshop.service.IFileCompressor;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService implements IProductService {

    @Value("${my-values.shortDescriptionMaxSymbols}")
    public int shortDescriptionMaxSymbols;
    @Value("${my-values.longDescriptionMaxSymbols}")
    public int longDescriptionMaxSymbols;

    private final ProductRepository productRepository;
    private final ProductMajorCategoryRepository majorCategoryRepository;
    private final ITextGenerator textGenerator;
    private final IFileCompressor imageCompressor;
    private final IFileService fileService;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductMajorCategoryRepository majorCategoryRepository,
                          ITextGenerator textGenerator,
                          IFileService fileService,
                          IFileCompressor imageCompressor) {
        this.productRepository = productRepository;
        this.majorCategoryRepository = majorCategoryRepository;
        this.textGenerator = textGenerator;
        this.fileService = fileService;
        this.imageCompressor = imageCompressor;
    }

    @Override
    public Long addNewProduct(ValidProduct validProduct) {
        ProductMajorCategory category = majorCategoryRepository
                .findByName(validProduct.getCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));

        Product product = parseProduct(validProduct, category);
        uploadAndAddImagesToProduct(validProduct.getImageFiles(), product);

        return productRepository.save(product).getId();
    }

    private Product parseProduct(ValidProduct validProduct, ProductMajorCategory category) {
        Product product = new Product();
        product.setTitle(validProduct.getTitle());
        product.setShortDescription(validProduct.getShortDescription());
        product.setDescription(validProduct.getDescription());
        product.setPrice(validProduct.getPrice());
        product.setCategory(category);
        product.setImageFiles(new ArrayList<>());
        if (validProduct.getCharacteristics() != null) {
            product.setCharacteristics(validProduct.getCharacteristics().trim());
        }
        product.setRating(0.);
        return product;
    }

    private void uploadAndAddImagesToProduct(List<MultipartFile> images, Product product) {
        if (images == null) return;
        for (MultipartFile imageFile : images) {
            if (imageFile.getSize() == 0
                    || imageFile.getOriginalFilename() == null
                    || imageFile.getOriginalFilename().isEmpty()) continue;

            String fullFilePath = UUID.randomUUID().toString();
            FileTransferInfo fileTransferInfo = FileTransferInfo.createFrom(fullFilePath, imageFile);
            imageCompressor.compressImageIfSupported(fileTransferInfo);
            fileService.uploadAsStream(fileTransferInfo);
            FileMeta fileMeta = fileService.saveMeta(fileTransferInfo);
            product.getImageFiles().add(fileMeta);
        }
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
    public void updateProductRatingFromReviews(Long productId) {
        Product product = getByIdOrThrow(productId);
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
    public Product getByIdOrThrow(Long productId) {
        return productRepository
                .findById(productId)
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
        rndShortDescription = rndShortDescription.substring(0, Math.min(rndShortDescription.length(), shortDescriptionMaxSymbols)).trim();
        rndLongDescription = rndLongDescription.substring(0, Math.min(rndLongDescription.length(), longDescriptionMaxSymbols)).trim();
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
