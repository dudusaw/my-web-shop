package com.example.mywebshop.repository;

import com.example.mywebshop.entity.ProductMajorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductMajorCategoryRepository extends JpaRepository<ProductMajorCategory, Long> {

    Optional<ProductMajorCategory> findByName(String name);
}
