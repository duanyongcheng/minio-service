package com.baary.minioservice.service;

import com.baary.minioservice.model.FileMiddlePathEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface IMinioService {
    /**
     * 上传文件返回文件
     * 以后返回文件id
     *
     * @param file               文件
     * @param fileMiddlePathEnum 文件中间目录名称
     * @return 返回path
     */
    String upload(MultipartFile file, FileMiddlePathEnum fileMiddlePathEnum);

    /**
     * 根据文件路径下载文件
     * @param path 文件路径
     * @param response 下载文件res
     */
    void downloadPath(String path, HttpServletResponse response);

    /**
     * 根据文件id下载文件
     * @param id 文件id
     * @param response 下载文件res
     */
    void download(String id, HttpServletResponse response);
}
