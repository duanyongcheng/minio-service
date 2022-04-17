package com.babary.minioservice.utils;

import cn.hutool.core.date.DateUtil;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class MinioClientUtil {

    @Value("${minio.minioUrl}")
    private String minioUrl;

    @Value("${minio.assessKey}")
    private String assessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    private MinioClient minioClient;

    @PostConstruct
    public void initClient() {
        if (this.minioClient == null) {
            try {
                this.minioClient = MinioClient.builder()
                        .endpoint(this.minioUrl)
                        .credentials(this.assessKey, this.secretKey)
                        .build();
                boolean found = this.minioClient.bucketExists(
                        BucketExistsArgs.builder().bucket(this.bucket).build()
                );
                if (found) {
                    log.info("bucket is found");
                } else {
                    this.minioClient.makeBucket(
                            MakeBucketArgs.builder().bucket(this.bucket).build()
                    );
                }
            } catch (Exception e) {
                log.error("client init error");
                System.out.println("error");
            }
        }
    }

    public void upload(File file) throws Exception{
        FileInputStream fileInputStream = new FileInputStream(file);
        PutObjectArgs putObject =
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(file.getPath())
                        .stream(fileInputStream, fileInputStream.available(), -1L)
                        .build();
        this.minioClient.putObject(putObject);
    }


    public void upload(MultipartFile file) throws Exception {
        PutObjectArgs putObject =
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(file.getOriginalFilename())
                        .stream(file.getInputStream(), file.getSize(), -1L)
                        .contentType(file.getContentType())
                        .build();
        this.minioClient.putObject(putObject);
    }


    private String getObjectName(String path){
        String now = DateUtil.now();
        return now + path;
    }

}
