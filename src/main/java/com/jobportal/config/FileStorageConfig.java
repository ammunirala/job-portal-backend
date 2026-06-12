package com.jobportal.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.*;

@Component
public class FileStorageConfig {

    public static final String UPLOAD_DIR = "uploads/resumes/";

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder");
        }
    }
}