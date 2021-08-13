package com.example.mywebshop.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mywebshop.dto.FileTransferInfo;
import com.example.mywebshop.entity.FileMeta;
import com.example.mywebshop.repository.FileMetaRepository;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

public class LocalFileStorageTest {
    @Test
    public void testGetFile() {
        // Arrange
        FileMetaRepository fileMetaRepository = mock(FileMetaRepository.class);
        when(fileMetaRepository.findByPath(anyString())).thenThrow(new UnsupportedOperationException("An error occurred"));

        // Act and Assert
        assertThrows(UnsupportedOperationException.class,
                () -> (new LocalFileStorage(fileMetaRepository)).getFile("/tmp/foo.txt"));
        verify(fileMetaRepository).findByPath(anyString());
    }

    @Test
    public void testUploadAsStream() {
        // Arrange
        LocalFileStorage localFileStorage = new LocalFileStorage(mock(FileMetaRepository.class));
        FileTransferInfo fileTransferInfo = mock(FileTransferInfo.class);
        when(fileTransferInfo.getFilePath()).thenThrow(new UnsupportedOperationException("An error occurred"));

        // Act and Assert
        assertThrows(UnsupportedOperationException.class, () -> localFileStorage.uploadAsStream(fileTransferInfo));
        verify(fileTransferInfo).getFilePath();
    }

    @Test
    public void testSaveMeta() throws UnsupportedEncodingException {
        // Arrange
        FileMeta fileMeta = new FileMeta();
        fileMeta.setContentType("text/plain");
        fileMeta.setOriginalFilename("foo.txt");
        fileMeta.setId(123L);
        fileMeta.setPath("Path");
        FileMetaRepository fileMetaRepository = mock(FileMetaRepository.class);
        when(fileMetaRepository.save((FileMeta) any())).thenReturn(fileMeta);
        LocalFileStorage localFileStorage = new LocalFileStorage(fileMetaRepository);

        // Act and Assert
        assertSame(fileMeta,
                localFileStorage
                        .saveMeta(new FileTransferInfo(new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8")),
                                "/tmp/foo.txt", "foo.txt", "Format", "text/plain", 3L)));
        verify(fileMetaRepository).save((FileMeta) any());
    }

    @Test
    public void testUpdateFileTags() {
        // Arrange, Act and Assert
        assertThrows(UnsupportedOperationException.class,
                () -> (new LocalFileStorage(mock(FileMetaRepository.class))).updateFileTags(123L));
        assertThrows(UnsupportedOperationException.class,
                () -> (new LocalFileStorage(mock(FileMetaRepository.class))).updateFileTags("/tmp/foo.txt"));
    }
}

