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
import com.epam.ems.entity.Certificate;
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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Profile("!dev")
public class S3ServiceImpl implements S3Service {

    private static final String BASE64_HEADER = "data:image/png;base64,";
    private static final String BUCKET_NAME = System.getenv("CERT_APP_BUCKET");
    private static final String DEFAULT_KEY = "images/default.png";
    private static final String ROOT_PATH = "images/";
    private static final String EXTENSION = ".png";
    private static final String FORMAT_TYPE = "png";
    private static final String GET_IMAGE_ERROR_CODE = "30801";
    private static final String UPLOAD_IMAGE_ERROR_CODE = "30802";
    private static final String DELETE_IMAGE_ERROR_CODE = "30803";
    private final CertificateService certificateService;
    private AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(System.getenv("BUCKET_REGION"))
            .withCredentials(new InstanceProfileCredentialsProvider(false))
            .build();


    public S3ServiceImpl(CertificateService certificateService) {
        this.certificateService = certificateService;
    }


    @Override
    public String getImageBase64(Long id) {
        certificateService.isCertificateExistById(id);
        Certificate certificate = certificateService.getCertificate(id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        S3Object s3Image;
        String key = ROOT_PATH + certificate.getImageMd5Sum() + EXTENSION;
        try {
            if (s3Client.doesObjectExist(BUCKET_NAME, key)) {
                GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME, key);
                s3Image = s3Client.getObject(getObjectRequest);
            } else {
                GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME, DEFAULT_KEY);
                s3Image = s3Client.getObject(getObjectRequest);
            }
            BufferedImage image = ImageIO.read(s3Image.getObjectContent());
            ImageIO.write(image, FORMAT_TYPE, outputStream);
            return BASE64_HEADER + Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, GET_IMAGE_ERROR_CODE);
        }
    }


    @Override
    public void uploadImage(Long id, MultipartFile multipartFile) {
        certificateService.isCertificateExistById(id);
        Certificate certificate = certificateService.getCertificate(id);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(multipartFile.getBytes());
            String imageHash = new BigInteger(1, digest).toString(16);
            certificate.setImageMd5Sum(imageHash);
            certificateService.updateCertificate(certificate);
            String key = ROOT_PATH + imageHash + EXTENSION;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getSize());
            PutObjectRequest putObjectRequest;
            putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, multipartFile.getInputStream(), metadata);
            s3Client.putObject(putObjectRequest);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, UPLOAD_IMAGE_ERROR_CODE);
        }
    }

    @Override
    public void deleteImage(String imageHash) {
        try {
            if (certificateService.couldBeImageDeleted(imageHash)) {
                DeleteObjectRequest deleteObjectRequest;
                String key = ROOT_PATH + imageHash + EXTENSION;
                if (s3Client.doesObjectExist(BUCKET_NAME, key)) {
                    deleteObjectRequest = new DeleteObjectRequest(BUCKET_NAME, key);
                    s3Client.deleteObject(deleteObjectRequest);
                }
            }
        } catch (SdkClientException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, DELETE_IMAGE_ERROR_CODE);
        }
    }

}
