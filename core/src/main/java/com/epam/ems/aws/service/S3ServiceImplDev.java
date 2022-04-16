package com.epam.ems.aws.service;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.epam.ems.aws.S3Service;
import com.epam.ems.service.CertificateService;
import com.epam.ems.service.exception.ServiceException;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

@Component
@Profile("dev")
public class S3ServiceImplDev implements S3Service {
    private final CertificateService certificateService;
    private static final String DEFAULT_KEY = "images/default.png";
    private AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion("us-east1")
            .withCredentials(new InstanceProfileCredentialsProvider(false))
            .build();
    private String bucketName = "bucket";

    public S3ServiceImplDev(CertificateService certificateService) {
        this.certificateService = certificateService;
    }


    @Override
    public String getImageBase64(Long id) {
        certificateService.isCertificateExistById(id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        S3Object s3Image;
        String key = "images/" + id + ".png";
        try {
            if (s3Client.doesObjectExist(bucketName, key)) {
                GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
                s3Image = s3Client.getObject(getObjectRequest);
            } else {
                GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, DEFAULT_KEY);
                s3Image = s3Client.getObject(getObjectRequest);
            }
            BufferedImage image = ImageIO.read(s3Image.getObjectContent());
            ImageIO.write(image, "png", outputStream);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "30801");
        }
    }


    @Override
    public void uploadImage(Long id, MultipartFile multipartFile) {
        certificateService.isCertificateExistById(id);
        String key = "images/" + id + ".png";
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
        PutObjectRequest putObjectRequest;
        try {
            putObjectRequest = new PutObjectRequest(bucketName, key, multipartFile.getInputStream(), metadata);
            s3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "30802");
        }
    }

}
