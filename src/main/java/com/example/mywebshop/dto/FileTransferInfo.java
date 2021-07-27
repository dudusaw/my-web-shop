package com.example.mywebshop.dto;

import com.example.mywebshop.utils.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Data
@AllArgsConstructor
public class FileTransferInfo {

    /**
     * Stream must be explicitly closed
     */
    private InputStream stream;

    private String filePath;

    private String originalFileName;

    private String format;

    private String contentType;

    private long contentLength;

    public static FileTransferInfo createFrom(String fullFilePath, MultipartFile file) {
        FileTransferInfo info = null;
        try {
            info = new FileTransferInfo();
            info.filePath = fullFilePath;
            info.stream = file.getInputStream();
            info.originalFileName = file.getOriginalFilename();
            info.format = Util.getFileFormat(file.getOriginalFilename());
            info.contentType = file.getContentType();
            info.contentLength = file.getSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    private FileTransferInfo() {
    }
}
