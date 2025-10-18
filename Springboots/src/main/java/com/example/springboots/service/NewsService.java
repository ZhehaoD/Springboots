package com.example.springboots.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboots.common.AuthAccess;
import com.example.springboots.entity.News;
import com.example.springboots.entity.User;
import com.example.springboots.exception.ServiceException;
import com.example.springboots.mapper.NewsMapper;
import com.example.springboots.mapper.UserMapper;
import com.example.springboots.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NewsService extends ServiceImpl<NewsMapper, News> {
    @Resource
    NewsMapper newsMapper;
}
