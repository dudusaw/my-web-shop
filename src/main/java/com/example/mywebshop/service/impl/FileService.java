package com.example.mywebshop.service.impl;

import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.repository.FileStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;

@Service
public class FileService implements com.example.mywebshop.service.IFileService {

    @Value("${my-values.image-location}")
    private String imageLocation;

    @Value("${my-values.image-compression-quality}")
    private float compressionQuality;

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
                String[] split = imageFile.getOriginalFilename().split("\\.");
                if (split.length == 1)
                    throw new IllegalArgumentException("image name has no extension " + imageFile.getOriginalFilename());
                String fileFormat = split[split.length - 1];

                Path destination = Paths.get(imageLocation).resolve(uuid + "." + fileFormat);
                Files.createDirectories(destination.subpath(0, destination.getNameCount() - 1));
                if (isFormatSupportedForCompression(fileFormat))
                    compressImage(imageFile, fileFormat, destination);
                else
                    imageFile.transferTo(destination);

                fileMeta = new FileMeta(destination.subpath(1, destination.getNameCount()).toString(), imageFile.getOriginalFilename());
                fileStoreRepository.save(fileMeta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileMeta;
    }

    private void compressImage(MultipartFile imageFile, String format, Path destination) {
        try {
            BufferedImage image = ImageIO.read(imageFile.getInputStream());

            File compressedImageFile = destination.toFile();
            OutputStream os = new FileOutputStream(compressedImageFile);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
            if (!writers.hasNext())
                throw new IllegalArgumentException("no such format supported " + format);
            ImageWriter writer = writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(compressionQuality);
            writer.write(null, new IIOImage(image, null, null), param);

            os.close();
            ios.close();
            writer.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFormatSupportedForCompression(String format) {
        return ImageIO.getImageWritersByFormatName(format).hasNext();
    }

    private boolean checkForEmpty(MultipartFile imageFile) {
        return imageFile != null && !imageFile.isEmpty()
                && imageFile.getOriginalFilename() != null && !imageFile.getOriginalFilename().isEmpty();
    }
}
