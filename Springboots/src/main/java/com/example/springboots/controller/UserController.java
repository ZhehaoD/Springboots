package com.example.springboots.controller;


import com.example.springboots.common.Page;
import com.example.springboots.common.Result;
import com.example.springboots.entity.User;
import com.example.springboots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")

public class UserController
{
    @Autowired
    UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody User user){
        userService.insertUser(user);
        return Result.success();
    }

    @PutMapping ("/update")
    public Result update(@RequestBody User user){
        userService.updateUser(user);
        return Result.success();
    }

    @DeleteMapping ("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        userService.deleteUser(id);
        return Result.success();
    }

    @DeleteMapping ("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids){
        userService.batchDeleteUser(ids);
        return Result.success();
    }

    @GetMapping ("/selectAll")
    public Result selectAll(){
        List<User> userList = userService.selectAll();
        return Result.success(userList);
    }

    @GetMapping ("/selectByID/{id}")
    public Result selectByID(@PathVariable Integer id){
        User user = userService.selectByID(id);
        return Result.success(user);
    }

    @GetMapping ("/selectByName/{name}")
    public Result selectByName(@PathVariable String name){
        List<User> user = userService.selectByName(name);
        return Result.success(user);
    }

    @GetMapping ("/selectByMore")
    public Result selectByMore(@RequestParam String username, @RequestParam String name){
        List<User> userList = userService.selectByMore(username,name);
        return Result.success(userList);
    }

    @GetMapping ("/selectByMo")
    public Result selectByMo(@RequestParam String username, @RequestParam String name){
        List<User> userList = userService.selectByMo(username,name);
        return Result.success(userList);
    }

    @GetMapping ("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam String name){
        userService.selectByPage(pageNum, pageSize, username, name)
        return Result.success(pageMap);
    }
}
