package com.example.mywebshop.service;

import com.example.mywebshop.dto.FileTransferInfo;

public interface IFileCompressor {
    /**
     * Compress the input stream from an image, returns true if success, false otherwise
     */
    boolean compressImageIfSupported(FileTransferInfo image);

    /**
     * Compress any file, return new stream inside {@link FileTransferInfo}
     */
    void compressFile(FileTransferInfo file);

    /**
     * Decompress any file, return new stream inside {@link FileTransferInfo},
     * should be previously compressed with compress.
     */
    void decompressFile(FileTransferInfo file);
}
