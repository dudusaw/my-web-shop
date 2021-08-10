package com.example.mywebshop.controller;

import com.example.mywebshop.dto.FileTransferInfo;
import com.example.mywebshop.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Controller
@Profile("!local-storage")
public class FileController {

    private final IFileService fileService;

    private static final CacheControl cacheControl = CacheControl
            .maxAge(365, TimeUnit.DAYS)
            .noTransform()
            .mustRevalidate();

    @Autowired
    public FileController(IFileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/file/**")
    public ResponseEntity<ByteArrayResource> getFile(HttpServletRequest request)
            throws IOException {
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String path = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        FileTransferInfo file = fileService.getFile(path);
        ByteArrayResource resultResource = new ByteArrayResource(file.getStream().readAllBytes());
        file.getStream().close();
        return ResponseEntity
                .ok()
                .cacheControl(cacheControl)
                .contentType(MediaType.valueOf(file.getContentType()))
                .body(resultResource);
    }
}
