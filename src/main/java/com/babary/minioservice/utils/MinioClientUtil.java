package com.babary.minioservice.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.babary.minioservice.model.FileMiddlePathEnum;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;

@Component
@Slf4j
public class MinioClientUtil {


    private static final String MINIO_PROTOCOL = "minio://";

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

    public void upload(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        PutObjectArgs putObject =
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(file.getPath())
                        .stream(fileInputStream, fileInputStream.available(), -1L)
                        .build();
        this.minioClient.putObject(putObject);
    }


    /**
     * 上传文件并返回文件路径
     * @param file 文件
     * @param fileMiddlePathEnum 中间奴鲁
     * @return 路径
     * @throws Exception 异常
     */
    public String upload(MultipartFile file, FileMiddlePathEnum fileMiddlePathEnum) throws Exception {
        String fileName = IdUtil.simpleUUID() + getExtension(file);
        String path = fileMiddlePathEnum.getPath() + fileName;
        String objectName = getObjectName(path);
        PutObjectArgs putObject =
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1L)
                        .contentType(file.getContentType())
                        .build();
        this.minioClient.putObject(putObject);
        return MINIO_PROTOCOL + objectName;
    }


    private String getObjectName(String path) {
        String now = DateUtil.now();
        return now + path;
    }

    /**
     * 获取文件的扩展名
     *
     * @param file 上传的文件
     * @return 返回后缀
     */
    private String getExtension(MultipartFile file) {
        String extension = FileNameUtils.getExtension(file.getOriginalFilename());
        if (StrUtil.isEmptyIfStr(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

}
