package com.example.springboots.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboots.entity.News;
import com.example.springboots.entity.Notice;
import com.example.springboots.mapper.NewsMapper;
import com.example.springboots.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {
    @Resource
    NoticeMapper noticeMapper;
}
