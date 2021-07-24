package com.example.mywebshop.repository;

import com.example.mywebshop.entity.FileMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileMetaRepository extends JpaRepository<FileMeta, Long> {

    List<FileMeta> findAllByOriginalFilename(String originalFilename);

    Optional<FileMeta> findByPath(String path);
}
