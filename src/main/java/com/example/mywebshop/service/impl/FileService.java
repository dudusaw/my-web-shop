package com.example.mywebshop.service.impl;

import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.repository.FileStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService implements com.example.mywebshop.service.IFileService {

    @Value("${my-values.image-location}")
    private String imageLocation;

    private final FileStoreRepository fileStoreRepository;

    @Autowired
    public FileService(FileStoreRepository fileStoreRepository) {
        this.fileStoreRepository = fileStoreRepository;
    }

    @Override
    public FileMeta saveImageFileIfExists(MultipartFile imageFile) {
        FileMeta fileMeta = null;
        try {
            if (checkForEmpty(imageFile)) {
                String uuid = UUID.randomUUID().toString();
                String filename = uuid + "__" + imageFile.getOriginalFilename();
                Path dest = Paths.get(imageLocation).resolve(filename);
                imageFile.transferTo(dest);
                fileMeta = new FileMeta(dest.subpath(1, dest.getNameCount()).toString(), imageFile.getOriginalFilename());
                fileStoreRepository.save(fileMeta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileMeta;
    }

    private boolean checkForEmpty(MultipartFile imageFile) {
        return imageFile != null && !imageFile.isEmpty()
                && imageFile.getOriginalFilename() != null && !imageFile.getOriginalFilename().isEmpty();
    }
}
