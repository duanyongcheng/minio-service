package com.babary.minioservice.controller;

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
    public void upload(MultipartFile file){
        minioService.upload(file);
    }

    @GetMapping
    public String isAlive(){
        return "yes i am alive";
    }
}
