package com.example.springboots.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboots.common.AuthAccess;
import com.example.springboots.entity.User;
import com.example.springboots.exception.ServiceException;
import com.example.springboots.mapper.UserMapper;
import com.example.springboots.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService extends ServiceImpl<UserMapper,User> {
    @Resource
    UserMapper userMapper;
    public User selectByUsername(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username); // eq=>== where username = #{username}
        return getOne(queryWrapper); // select * from user where username =#{username}
    }

    @AuthAccess
    public User login(User user) {
        //根据用户名查询数据库的用户信息
        User dbUser = selectByUsername(user.getUsername());
        if(dbUser==null){
            throw new ServiceException("用户名或密码错误");
        }
        if(!user.getPassword().equals(dbUser.getPassword())){
            throw new ServiceException("用户名或密码错误");
        }
        String token= TokenUtils.createToken(dbUser.getId().toString(),dbUser.getPassword());
        dbUser.setToken(token);
        System.out.println(dbUser);
        return dbUser;
    }

    public User register(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if(dbUser != null){
            throw new ServiceException("用户名已存在");
        }
        user.setName(user.getUsername());
        userMapper.insert(user);
        return user;
    }

    public void resetPassword(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if(dbUser==null){
            throw new ServiceException("用户名不存在");
        }
        if(!user.getPhone().equals(dbUser.getPhone())){
            throw new ServiceException("验证错误");
        }
        dbUser.setPassword("123");
        updateById(dbUser);
    }
}
