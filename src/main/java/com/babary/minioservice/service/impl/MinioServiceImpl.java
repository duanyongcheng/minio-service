package com.babary.minioservice.service.impl;

import com.babary.minioservice.service.IMinioService;
import com.babary.minioservice.utils.MinioClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class MinioServiceImpl implements IMinioService {

    public final MinioClientUtil minioClientUtil;

    public MinioServiceImpl(MinioClientUtil minioClientUtil) {
        this.minioClientUtil = minioClientUtil;
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            minioClientUtil.upload(file);
            return file.getOriginalFilename();
        } catch (Exception e) {
            log.error(e + "");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downloadPath(String path, HttpServletResponse response) {

    }

    @Override
    public void download(String id, HttpServletResponse response) {

    }
}
