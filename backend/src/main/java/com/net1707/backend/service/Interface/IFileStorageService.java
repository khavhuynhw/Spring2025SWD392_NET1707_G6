package com.net1707.backend.service.Interface;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileStorageService {
    String saveFile(MultipartFile file) throws IOException;
    void deleteFile(String imageUrl) throws IOException;

}
