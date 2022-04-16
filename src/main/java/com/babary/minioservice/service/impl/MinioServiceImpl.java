package com.babary.minioservice.service.impl;

import com.babary.minioservice.service.IMinioService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Service
public class MinioServiceImpl implements IMinioService {
    @Override
    public String upload(MultipartFile file) {
        return null;
    }

    @Override
    public void downloadPath(String path, HttpServletResponse response) {

    }

    @Override
    public void download(String id, HttpServletResponse response) {

    }
}
