package com.baary.minioservice.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baary.minioservice.model.FileMiddlePathEnum;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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

    public static String clearFilePath(String path) {
        if (path.contains(MINIO_PROTOCOL)) {
            path = path.replace(MINIO_PROTOCOL, "");
        }
        return path;
    }

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

    @Deprecated
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
     *
     * @param file               文件
     * @param fileMiddlePathEnum 中间奴鲁
     * @return 路径
     * @throws Exception 异常
     */
    public String upload(MultipartFile file, FileMiddlePathEnum fileMiddlePathEnum) throws Exception {
        String fileName = IdUtil.simpleUUID() + "." + getExtension(file);
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
        String now = DateUtil.today();
        return now + path;
    }

    private boolean isMinio(String path) {
        if (StrUtil.isEmptyIfStr(path)) {
            return false;
        }
        return path.startsWith(MINIO_PROTOCOL);
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
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

    public void downloadFile(String path, HttpServletResponse response) {
        try {
            GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(path).build());
            byte[] buf = new byte[1024];
            int length;
            response.reset();
            ;
            String filename = path.substring(path.lastIndexOf("/") + 1);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            response.setHeader("Accept-Ranges", "bytes");
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            ServletOutputStream outputStream = response.getOutputStream();
            while ((length = object.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            outputStream.close();
        } catch (Exception e) {
            log.error(StrUtil.format("file download error ,path :{}", path));
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String data = "文件下载失败";
            OutputStream op = null;
            try {
                op.write(data.getBytes(StandardCharsets.UTF_8));
            } catch (Exception exception) {
                log.error(e.getMessage());
            }
        }
    }
}
