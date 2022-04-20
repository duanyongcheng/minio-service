package com.babary.minioservice.controller;

import com.babary.minioservice.model.FileMiddlePathEnum;
import com.babary.minioservice.service.IMinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/minio")
public class MinioServiceController {


    @Autowired
    private IMinioService minioService;

    @PostMapping("/upload")
    public void upload(MultipartFile file, FileMiddlePathEnum fileMiddlePathEnum){
        if (fileMiddlePathEnum == null){
            fileMiddlePathEnum = FileMiddlePathEnum.PROFILE_PATH;
        }
        minioService.upload(file,fileMiddlePathEnum);
    }


    @PostMapping("/download")
    public void upload(String filePath){
    }

    @GetMapping
    public String isAlive(){
        return "yes i am alive";
    }
}
