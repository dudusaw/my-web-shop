package com.example.mywebshop.service.impl;

import com.example.mywebshop.config.exception.NotFoundException;
import com.example.mywebshop.dto.FileTransferInfo;
import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.repository.FileMetaRepository;
import com.example.mywebshop.service.IFileService;
import com.example.mywebshop.utils.Util;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MinioFileManager implements IFileService {

    private final FileMetaRepository fileMetaRepository;

    private MinioClient minioClient;
    private String bucketName;

    @Value("${minio.keep-object-tags}")
    private boolean keepObjectTags;

    private static final String DEFAULT_CONTENT_TYPE = MediaType.TEXT_PLAIN_VALUE;

    // tag keys
    private static final String CONTENT_TYPE = "contentType";
    private static final String ORIGINAL_FILE_NAME = "originalFileName";

    @Autowired
    public MinioFileManager(FileMetaRepository fileMetaRepository,
                            @Value("${minio.endpoint}") String endpoint,
                            @Value("${minio.username}") String username,
                            @Value("${minio.password}") String password,
                            @Value("${minio.bucket-name}") String bucketName) throws Exception {
        this.fileMetaRepository = fileMetaRepository;
        this.bucketName = bucketName;
        initMinioClient(endpoint, username, password, bucketName);
    }

    private void initMinioClient(String endpoint, String username, String password, String bucketName)
            throws Exception {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(username, password)
                .build();
        if (!minioClient.bucketExists(
                BucketExistsArgs
                        .builder()
                        .bucket(bucketName)
                        .build())) {
            minioClient.makeBucket(MakeBucketArgs
                    .builder()
                    .bucket(bucketName)
                    .build());
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build());
            fileMetaRepository
                    .findByPath(filePath)
                    .ifPresent(fileMetaRepository::delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileTransferInfo getFile(String filePath) {
        try {
            GetObjectResponse objectResponse = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build());
            FileMeta fileMeta = fileMetaRepository
                    .findByPath(filePath)
                    .orElseThrow();
            String fileName = fileMeta.getOriginalFilename();
            String contentType = fileMeta.getContentType();

            return new FileTransferInfo(
                    objectResponse,
                    filePath,
                    fileName,
                    Util.getFileFormat(fileName),
                    contentType,
                    -1
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public void uploadAsStream(FileTransferInfo file) {
        try {
            long contentLength = file.getContentLength() > 0 ? file.getContentLength() : -1;
            long partSize = contentLength > 0 ? -1 : 10485760;
            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(file.getFilePath())
                    .stream(file.getStream(), contentLength, partSize)
                    .contentType(file.getContentType());
            if (keepObjectTags) {
                var meta = constructTagsMap(
                        Pair.of(CONTENT_TYPE, file.getContentType()),
                        Pair.of(ORIGINAL_FILE_NAME, file.getOriginalFileName())
                );
                builder.tags(meta);
            }
            minioClient.putObject(builder.build());
            file.getStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileMeta saveToDB(FileTransferInfo file) {
        FileMeta meta = new FileMeta(
                file.getFilePath(),
                file.getOriginalFileName(),
                file.getContentType());
        meta = fileMetaRepository.save(meta);
        return meta;
    }

    @Override
    public void updateFileTags(Long fileMetaId) {
        FileMeta fileMeta = fileMetaRepository
                .findById(fileMetaId)
                .orElseThrow(NotFoundException::new);
        updateFileTags(fileMeta);
    }

    @Override
    public void updateFileTags(String filePath) {
        FileMeta fileMeta = fileMetaRepository
                .findByPath(filePath)
                .orElseThrow(NotFoundException::new);
        updateFileTags(fileMeta);
    }

    private void updateFileTags(FileMeta fileMeta) {
        Map<String, String> tags = constructTagsMap(
                Pair.of(CONTENT_TYPE, fileMeta.getContentType()),
                Pair.of(ORIGINAL_FILE_NAME, fileMeta.getOriginalFilename())
        );
        try {
            minioClient.setObjectTags(SetObjectTagsArgs.builder()
                    .bucket(bucketName)
                    .object(fileMeta.getPath())
                    .tags(tags)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SafeVarargs
    private Map<String, String> constructTagsMap(Pair<String, String>... pairs) {
        HashMap<String, String> map = new HashMap<>();
        for (var pair : pairs) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }
}
