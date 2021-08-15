package com.example.mywebshop.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mywebshop.dto.FileTransferInfo;

import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

public class FileCompressorTest {
    @Test
    public void testCompressImageIfSupported() throws UnsupportedEncodingException {
        // Arrange
        FileCompressor fileCompressor = new FileCompressor();

        // Act and Assert
        assertFalse(fileCompressor.compressImageIfSupported(
                new FileTransferInfo(new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8")), "/tmp/foo.txt",
                        "foo.txt", "Format", "text/plain", 3L)));
    }

    @Test
    public void testCompressImageIfSupported2() {
        // Arrange
        FileCompressor fileCompressor = new FileCompressor();

        // Act and Assert
        assertFalse(fileCompressor
                .compressImageIfSupported(new FileTransferInfo(null, "/tmp/foo.txt", "foo.txt", "Format", "text/plain", 3L)));
    }

    @Test
    public void testCompressImageIfSupported3() throws UnsupportedEncodingException {
        // Arrange
        FileCompressor fileCompressor = new FileCompressor();
        FileTransferInfo fileTransferInfo = mock(FileTransferInfo.class);
        when(fileTransferInfo.getFormat()).thenReturn("foo");
        when(fileTransferInfo.getStream())
                .thenReturn(new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8")));

        // Act and Assert
        assertFalse(fileCompressor.compressImageIfSupported(fileTransferInfo));
        verify(fileTransferInfo, times(2)).getFormat();
        verify(fileTransferInfo).getStream();
    }

    @Test
    public void testCompressFile() {
        // Arrange
        FileCompressor fileCompressor = new FileCompressor();
        FileTransferInfo fileTransferInfo = new FileTransferInfo(null, "/tmp/foo.txt", "foo.txt", "Format", "text/plain",
                3L);

        // Act
        fileCompressor.compressFile(fileTransferInfo);

        // Assert that nothing has changed
        assertEquals(3L, fileTransferInfo.getContentLength());
        assertEquals("foo.txt", fileTransferInfo.getOriginalFileName());
        assertEquals("Format", fileTransferInfo.getFormat());
        assertEquals("/tmp/foo.txt", fileTransferInfo.getFilePath());
        assertEquals("text/plain", fileTransferInfo.getContentType());
    }

    @Test
    public void testCompressFile2() throws UnsupportedEncodingException {
        // Arrange
        FileCompressor fileCompressor = new FileCompressor();
        FileTransferInfo fileTransferInfo = mock(FileTransferInfo.class);
        doNothing().when(fileTransferInfo).setStream(any());
        when(fileTransferInfo.getStream())
                .thenReturn(new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8")));

        // Act
        fileCompressor.compressFile(fileTransferInfo);

        // Assert
        verify(fileTransferInfo, times(3)).getStream();
        verify(fileTransferInfo).setStream(any());
    }

    @Test
    public void testCompressFile3() {
        // Arrange
        FileCompressor fileCompressor = new FileCompressor();
        FileTransferInfo fileTransferInfo = mock(FileTransferInfo.class);
        doNothing().when(fileTransferInfo).setStream(any());
        when(fileTransferInfo.getStream()).thenReturn(new FileInputStream(new FileDescriptor()));

        // Act
        fileCompressor.compressFile(fileTransferInfo);

        // Assert
        verify(fileTransferInfo, times(2)).getStream();
    }

    @Test
    public void testDecompressFile() throws UnsupportedEncodingException {
        // Arrange
        FileCompressor fileCompressor = new FileCompressor();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8"));
        FileTransferInfo fileTransferInfo = new FileTransferInfo(byteArrayInputStream, "/tmp/foo.txt", "foo.txt", "Format",
                "text/plain", 3L);

        // Act
        fileCompressor.decompressFile(fileTransferInfo);

        // Assert that nothing has changed
        assertEquals(3L, fileTransferInfo.getContentLength());
        assertSame(byteArrayInputStream, fileTransferInfo.getStream());
        assertEquals("foo.txt", fileTransferInfo.getOriginalFileName());
        assertEquals("text/plain", fileTransferInfo.getContentType());
        assertEquals("Format", fileTransferInfo.getFormat());
        assertEquals("/tmp/foo.txt", fileTransferInfo.getFilePath());
    }

    @Test
    public void testDecompressFile2() {
        // Arrange
        FileCompressor fileCompressor = new FileCompressor();
        FileTransferInfo fileTransferInfo = new FileTransferInfo(null, "/tmp/foo.txt", "foo.txt", "Format", "text/plain",
                3L);

        // Act
        fileCompressor.decompressFile(fileTransferInfo);

        // Assert that nothing has changed
        assertEquals(3L, fileTransferInfo.getContentLength());
        assertEquals("foo.txt", fileTransferInfo.getOriginalFileName());
        assertEquals("Format", fileTransferInfo.getFormat());
        assertEquals("/tmp/foo.txt", fileTransferInfo.getFilePath());
        assertEquals("text/plain", fileTransferInfo.getContentType());
    }

    @Test
    public void testDecompressFile3() throws UnsupportedEncodingException {
        // Arrange
        FileCompressor fileCompressor = new FileCompressor();
        FileTransferInfo fileTransferInfo = mock(FileTransferInfo.class);
        when(fileTransferInfo.getStream())
                .thenReturn(new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8")));

        // Act
        fileCompressor.decompressFile(fileTransferInfo);

        // Assert
        verify(fileTransferInfo, times(2)).getStream();
    }
}

