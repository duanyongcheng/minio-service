package com.baary.minioservice.controller;

import com.baary.minioservice.model.FileDownloadDto;
import com.baary.minioservice.model.FileMiddlePathEnum;
import com.baary.minioservice.service.IMinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/minio")
public class MinioServiceController {


    @Autowired
    private IMinioService minioService;

    @PostMapping("/upload")
    public String upload(MultipartFile file, FileMiddlePathEnum fileMiddlePathEnum){
        if (fileMiddlePathEnum == null){
            fileMiddlePathEnum = FileMiddlePathEnum.PROFILE_PATH;
        }
        return minioService.upload(file,fileMiddlePathEnum);
    }


    @PostMapping("/download")
    public void upload(@RequestBody FileDownloadDto downloadDto, HttpServletResponse response){
        this.minioService.downloadPath(downloadDto.getFilePath(),response);
    }

    @GetMapping
    public String isAlive(){
        return "yes i am alive";
    }
}
