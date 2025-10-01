package com.example.springboots.controller;


import com.example.springboots.common.Result;
import com.example.springboots.entity.User;
import com.example.springboots.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController


public class WebController {
    @Resource
    UserService userService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("success");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if(user.getUsername()==null || user.getUsername().isEmpty()){
            return Result.error("数据输入不合法");
        }
        user = userService.login(user);
        return Result.success(user);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if(user.getUsername()==null || user.getUsername().isEmpty()){
            return Result.error("数据输入不合法");
        }
        if(user.getUsername().length()>10 || user.getPassword().length()>20){
            return Result.error("数据输入不合法");
        }
        user = userService.register(user);
        return Result.success(user);
    }
}
