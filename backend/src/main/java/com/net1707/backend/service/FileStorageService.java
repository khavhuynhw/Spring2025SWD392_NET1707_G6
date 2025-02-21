package com.net1707.backend.service;

import com.net1707.backend.service.Interface.IFileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService implements IFileStorageService {
    @Value("${upload.dir}")
    private String uploadDir;
    @Override
    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes());

        return "/uploads/" + fileName;
    }

    @Override
    public void deleteFile(String imageUrl) throws IOException {
        if (imageUrl != null) {
            String oldFileName = Paths.get(imageUrl).getFileName().toString();
            Path oldFilePath = Paths.get(uploadDir, oldFileName);
            Files.deleteIfExists(oldFilePath);
        }
    }
}
