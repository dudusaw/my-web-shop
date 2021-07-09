package com.example.mywebshop.repository;

import com.example.mywebshop.entity.FileMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStoreRepository extends JpaRepository<FileMeta, Long> {
}
