package com.example.springboots.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboots.entity.Logs;
import com.example.springboots.entity.News;
import com.example.springboots.mapper.LogsMapper;
import com.example.springboots.mapper.NewsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LogsService extends ServiceImpl<LogsMapper, Logs> {

}
