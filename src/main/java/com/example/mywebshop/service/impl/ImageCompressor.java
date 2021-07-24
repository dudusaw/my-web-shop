package com.example.mywebshop.service.impl;

import com.example.mywebshop.dto.FileTransferInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
@Slf4j
public class ImageCompressor implements com.example.mywebshop.service.IImageCompressor {

    @Value("${my-values.image-compression-quality}")
    private float compressionQuality;

    @Override
    public boolean compressImageIfSupported(FileTransferInfo image) {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(image.getFormat());
        if (!writers.hasNext()) {
            log.warn("no such format supported " + image.getFormat());
            return false;
        }
        ImageWriter writer = writers.next();

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedImage bufferedImage = ImageIO.read(image.getStream());
            image.getStream().close();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(compressionQuality);
            writer.write(null, new IIOImage(bufferedImage, null, null), param);

            byte[] buf = os.toByteArray();
            ByteArrayInputStream compressedImageStream = new ByteArrayInputStream(buf);
            image.setStream(compressedImageStream);
            image.setContentLength(os.size());

            os.close();
            ios.close();
            writer.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
