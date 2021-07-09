package com.example.mywebshop.service;

import com.example.mywebshop.entity.FileMeta;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    FileMeta saveImageFileIfExists(MultipartFile imageFile);
}
