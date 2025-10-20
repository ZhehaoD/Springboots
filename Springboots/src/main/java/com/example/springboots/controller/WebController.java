package com.example.springboots.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.example.springboots.common.AuthAccess;
import com.example.springboots.common.HoneyLogs;
import com.example.springboots.common.LogType;
import com.example.springboots.common.Result;
import com.example.springboots.entity.Orders;
import com.example.springboots.entity.User;
import com.example.springboots.service.OrdersService;
import com.example.springboots.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController


public class WebController {
    @Resource
    UserService userService;

    @Resource
    OrdersService ordersService;
    @GetMapping("/")
    public Result hello() {
        return Result.success("success");
    }

    @HoneyLogs(operation = "用户", type = LogType.LOGIN)
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if(user.getUsername()==null || user.getUsername().isEmpty()){
            return Result.error("数据输入不合法");
        }
        user = userService.login(user);
        return Result.success(user);
    }

    @HoneyLogs(operation = "用户", type = LogType.REGISTER)
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if(user.getUsername()==null || user.getUsername().isEmpty() || user.getRole().isEmpty()){
            return Result.error("数据输入不合法");
        }
        if(user.getUsername().length()>10 || user.getPassword().length()>20){
            return Result.error("数据输入不合法");
        }
        user = userService.register(user);
        return Result.success(user);
    }

    @AuthAccess
    @HoneyLogs(operation = "用户", type = LogType.UPDATE)
    @PutMapping ("/password")
    public Result password(@RequestBody User user) {
        if(user.getUsername()==null || user.getPhone()==null){
            return Result.error("数据输入不合法");
        }
        userService.resetPassword(user);
        return Result.success(user);
    }

    @GetMapping("/charts")
    public Result charts() {
        List<Orders> list = ordersService.list();
        // 提取所有不重复的日期，并排序
        Set<String> dateSet = list.stream().map(Orders::getDate).collect(Collectors.toSet());
        List<String> sortedDates = new ArrayList<>(dateSet);
        sortedDates.sort(Comparator.naturalOrder()); // 按自然顺序（如日期字符串）排序

        List<Dict> lineList = new ArrayList<>();
        for (String date : sortedDates) {
            // 计算当前日期的总金额
            BigDecimal dailySum = list.stream()
                    .filter(order -> date.equals(order.getDate())) // 过滤当前日期的订单
                    .map(Orders::getMoney)
                    .reduce(BigDecimal::add) // 累加金额
                    .orElse(BigDecimal.ZERO); // 无数据时默认为0

            // 每个Dict对应一个日期和其总金额（关键修正：用当前date而非dates集合）
            Dict dict = Dict.create().set("date", date).set("value", dailySum);
            lineList.add(dict);
        }

        List<Dict> barList = new ArrayList<>();
        Set<String> categories = list.stream().map(Orders::getCategory).collect(Collectors.toSet());
        for(String cate: categories){
            BigDecimal sum = list.stream().filter(orders -> orders.getCategory().equals(cate)).map(Orders::getMoney).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            Dict dict = Dict.create();
            Dict bar = dict.set("name", cate).set("value", sum);
            barList.add(bar);
        }
        Dict res = Dict.create().set("line", lineList).set("bar", barList);
        return Result.success(res);
    }
}
