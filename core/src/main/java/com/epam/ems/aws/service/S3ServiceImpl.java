package com.epam.ems.aws.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.epam.ems.aws.S3Service;
import com.epam.ems.service.exception.ServiceException;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

@Component
public class S3ServiceImpl implements S3Service {
    private final String DEFAULT_KEY = "images/default.png";
    private AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(System.getenv("BUCKET_REGION"))
            .withCredentials(new InstanceProfileCredentialsProvider(false))
            .build();
    private String bucketName = System.getenv("CERT_APP_BUCKET");

    @Override
    public String getImageBase64(Long id) {
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
    public void uploadImage(String id, MultipartFile multipartFile) {
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

    @Override
    public void deleteImage(Long id) {
        DeleteObjectRequest deleteObjectRequest;
        String key = "images/" + id + ".png";
        try {
            if (s3Client.doesObjectExist(bucketName, key)) {
                deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
                s3Client.deleteObject(deleteObjectRequest);
            }
        } catch (SdkClientException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "30803");
        }
    }

}
