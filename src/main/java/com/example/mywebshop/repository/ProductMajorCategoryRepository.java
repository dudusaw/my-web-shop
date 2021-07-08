package com.example.mywebshop.repository;

import com.example.mywebshop.entity.ProductMajorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMajorCategoryRepository extends JpaRepository<ProductMajorCategory, Long> {
}
