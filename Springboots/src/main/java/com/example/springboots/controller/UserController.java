package com.example.springboots.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        userService.save(user);
        return Result.success();
    }

    @PutMapping ("/update")
    public Result update(@RequestBody User user){
        userService.updateById(user);
        return Result.success();
    }

    @DeleteMapping ("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        userService.removeById(id);
        return Result.success();
    }

    @DeleteMapping ("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids){
        userService.removeBatchByIds(ids);
        return Result.success();
    }

    @GetMapping ("/selectAll")
    public Result selectAll(){
        List<User> userList = userService.list(new QueryWrapper<User>().orderByAsc("id"));
        return Result.success(userList);
    }

    @GetMapping ("/selectByID/{id}")
    public Result selectByID(@PathVariable Integer id){
        User user = userService.getById(id);
        return Result.success(user);
    }


    @GetMapping ("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam String name){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByAsc("id");
        queryWrapper.like(StrUtil.isNotBlank(username),"username",username);
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }
}
