package com.epam.ems.aws;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String getImageBase64(Long id);
    void uploadImage(String id, MultipartFile imageDTO);
    void deleteImage(Long id);
}
