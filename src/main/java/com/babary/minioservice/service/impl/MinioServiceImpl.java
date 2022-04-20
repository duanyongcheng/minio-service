package com.babary.minioservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.babary.minioservice.model.FileMiddlePathEnum;
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
    public String upload(MultipartFile file, FileMiddlePathEnum fileMiddlePathEnum) {
        try {
            return minioClientUtil.upload(file, fileMiddlePathEnum);
        } catch (Exception e) {
            log.error(e + "");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downloadPath(String path, HttpServletResponse response) {
        log.info(StrUtil.format("start to download file ,path :{}", path));
        path = MinioClientUtil.clearFilePath(path);
        minioClientUtil.downloadFile(path,response);
        log.info("file download over");
    }

    @Override
    public void download(String id, HttpServletResponse response) {

    }
}
