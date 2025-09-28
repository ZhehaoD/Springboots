package com.example.springboots.controller;


import com.example.springboots.common.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能: 提供接口返回数据
 **/

@RestController
public class WebController {

    @RequestMapping
    public Result hello(){
        return Result.success("hello world");
    }
}
