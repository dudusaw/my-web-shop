package com.example.mywebshop.service;

import com.example.mywebshop.dto.FileTransferInfo;

public interface IImageCompressor {
    /**
     * Compresses the input stream from an image, returns true if success, false otherwise
     */
    boolean compressImageIfSupported(FileTransferInfo image);
}
