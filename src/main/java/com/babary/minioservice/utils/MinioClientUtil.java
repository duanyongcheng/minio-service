package com.babary.minioservice.utils;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
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
                if (found){
                    log.info("bucket is found");
                }else {
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

}
