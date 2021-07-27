package com.example.mywebshop.service;

import com.example.mywebshop.dto.FileTransferInfo;

public interface IFileCompressor {
    /**
     * Compresses and changes the input stream from an image, returns true if success, false otherwise
     */
    boolean compressImageIfSupported(FileTransferInfo image);

    void compressFile(FileTransferInfo file);

    void decompressFile(FileTransferInfo file);
}
