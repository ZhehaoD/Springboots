package com.example.springboots.service;


import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.springboots.common.Page;
import com.example.springboots.entity.User;
import com.example.springboots.exception.ServiceException;
import com.example.springboots.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public void insertUser(User user){
        userMapper.insert(user);
    }

    public void updateUser(User user){
        userMapper.updateUser(user);
    }

    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    public void batchDeleteUser(List<Integer> ids) {
        for(Integer id:ids){
            userMapper.deleteUser(id);
        }
    }

    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    public User selectByID(Integer id) {
        return userMapper.selectByID(id);
    }

    public List<User> selectByName(String name) {
        return userMapper.selectByName(name);
    }

    public List<User> selectByMore(String username, String name) {
        return userMapper.selectByMore(username,name);
    }

    public List<User> selectByMo(String username, String name) {
        return userMapper.selectByMo(username,name);
    }

    public Page<User> selectByPage(Integer pageNum, Integer pageSize, String username, String name) {
        Integer skipNum = (pageNum-1) * pageSize;

        Page<User> page = new Page<>();
        List<User> userList = userMapper.selectByPage(skipNum,pageSize,username,name);
        Integer total = userMapper.selectCountByPage(username,name);
        page.setTotal(total);
        page.setList(userList);
        return page;
    }

    public User login(User user) {
        //根据用户名查询数据库的用户信息
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser==null){
            throw new ServiceException("用户名或密码错误");
        }
        if(!user.getPassword().equals(dbUser.getPassword())){
            throw new ServiceException("用户名或密码错误");
        }
        String token= com.example.springboot.utils.TokenUtils.createToken(dbUser.getId().toString(),dbUser.getPassword());
        dbUser.setToken(token);
        System.out.println(dbUser);
        return dbUser;
    }

    public User register(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser != null){
            throw new ServiceException("用户名已存在");
        }
        user.setName(user.getUsername());
        userMapper.insert(user);
        return user;
    }
}
