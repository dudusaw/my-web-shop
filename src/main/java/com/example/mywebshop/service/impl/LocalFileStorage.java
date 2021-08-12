package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.NotFoundException;
import com.example.mywebshop.dto.FileTransferInfo;
import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.repository.FileMetaRepository;
import com.example.mywebshop.service.IFileService;
import com.example.mywebshop.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Profile("local-storage")
public class LocalFileStorage implements IFileService {

    @Value("${my-values.storage-location}")
    private String storeLocation;

    private final FileMetaRepository fileMetaRepository;

    @Autowired
    public LocalFileStorage(FileMetaRepository fileMetaRepository) {
        this.fileMetaRepository = fileMetaRepository;
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(getFinalPath(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileTransferInfo getFile(String filePath) {
        try {
            FileMeta fileMeta = fileMetaRepository.findByPath(filePath).orElseThrow(NotFoundException::new);
            InputStream stream = Files.newInputStream(getFinalPath(filePath));

            return new FileTransferInfo(
                    stream,
                    filePath,
                    fileMeta.getOriginalFilename(),
                    Util.getFileFormat(fileMeta.getOriginalFilename()),
                    fileMeta.getContentType(),
                    stream.available()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void uploadAsStream(FileTransferInfo file) {
        try {
            Path finalPath = getFinalPath(file.getFilePath());
            OutputStream outputStream = Files.newOutputStream(finalPath);
            file.getStream().transferTo(outputStream);
            file.getStream().close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public FileMeta saveMeta(FileTransferInfo file) {
        FileMeta meta = new FileMeta(
                file.getFilePath(),
                file.getOriginalFileName(),
                file.getContentType());
        return fileMetaRepository.save(meta);
    }

    @Override
    public void updateFileTags(Long fileMetaId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateFileTags(String filePath) {
        throw new UnsupportedOperationException();
    }

    private Path getFinalPath(String filePathRelative) {
        return Paths.get(storeLocation).resolve(filePathRelative);
    }
}
