package com.example.springboots.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboots.entity.Logs;
import com.example.springboots.entity.Orders;
import com.example.springboots.mapper.LogsMapper;
import com.example.springboots.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> {

}
