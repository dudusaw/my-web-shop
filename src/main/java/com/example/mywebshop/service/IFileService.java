package com.example.mywebshop.service;

import com.example.mywebshop.dto.FileTransferInfo;
import com.example.mywebshop.entity.FileMeta;

public interface IFileService {
    void deleteFile(String filePath);

    FileTransferInfo getFile(String filePath);

    void uploadAsStream(FileTransferInfo file);

    FileMeta saveToDB(FileTransferInfo file);

    void updateFileTags(Long fileMetaId);

    void updateFileTags(String filePath);
}
