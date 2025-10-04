package com.example.springboots.controller;

import cn.hutool.core.io.FileUtil;
import com.example.springboots.common.AuthAccess;
import com.example.springboots.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController

@RequestMapping("/file")

public class FileController {
    @Value("${ip}")
    String ip;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";
    @AuthAccess
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();//文件原始名称
        //eg. aaa.png
        String mainName = FileUtil.mainName(originalFilename);//aaa
        String extName = FileUtil.extName("文件的后缀");//png
        File parentFile = new File(ROOT_PATH);
        if(!parentFile.exists()){
            parentFile.mkdir();//如果当前文件的父级目录不存在，就创建
        }
        if(FileUtil.exist(ROOT_PATH + File.separator + originalFilename)){ //当前上传的文件已经存在，那么这个时候就要重名一个文件名称
            originalFilename = System.currentTimeMillis() + "_" + originalFilename;
        }
        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        file.transferTo(saveFile); //存储文件到本地的磁盘里面去
        String url = "http://" + ip + ":8080/file/download/" + originalFilename;
        return Result.success(url);//返回文件链接
    }

    @GetMapping("/download/{filename}")
    @AuthAccess
    public void download(@PathVariable String filename, HttpServletResponse response) throws IOException {
        String filePath = ROOT_PATH + File.separator + filename;
        if(!FileUtil.exist(filePath)){
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);//数组是一个字节数组，文件的字节流数组
        outputStream.flush();
        outputStream.close();
    }
}
