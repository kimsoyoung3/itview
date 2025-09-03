package com.example.itview_spring.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3Uploader {

    private final S3Client s3Client;
    private final String bucketName;
    private final Region region;

    public S3Uploader(
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.secret-key}") String secretKey,
            @Value("${aws.bucket-name}") String bucketName,
            @Value("${aws.region}") String regionName
    ) {
        this.bucketName = bucketName;
        this.region = Region.of(regionName);

        this.s3Client = S3Client.builder()
                .region(this.region)
                .credentialsProvider(
                    StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                    )
                )
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = getUniqueFileName(file.getOriginalFilename());
        byte[] resizedImage = resizeAndCompressImage(file);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(resizedImage));

        return getFileUrl(key);
    }

    public String getFileUrl(String keyName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region.id(), keyName);
    }

    public String getUniqueFileName(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }

    public byte[] resizeAndCompressImage(MultipartFile file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Thumbnails.of(file.getInputStream())
                .size(500, 500)
                .outputFormat("jpg")
                .outputQuality(0.4)
                .toOutputStream(baos);

        return baos.toByteArray();
    }

    public String extractKeyFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("fileUrl이 비어있습니다.");
        }
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

    public void deleteFile(String Url) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(extractKeyFromUrl(Url))
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}