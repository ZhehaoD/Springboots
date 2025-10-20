package com.example.springboots.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboots.common.HoneyLogs;
import com.example.springboots.common.LogType;
import com.example.springboots.common.Result;
import com.example.springboots.entity.Orders;
import com.example.springboots.entity.User;
import com.example.springboots.service.OrdersService;
import com.example.springboots.service.UserService;
import com.example.springboots.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 * 处理用户的增删改查、分页查询等请求
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    // 注入用户服务层（MyBatis-Plus 生成的 Service）
    @Autowired
    private OrdersService ordersService;

    @Autowired
    UserService userService;
    /**
     * 新增用户（含默认值填充）
     * 解决原代码中“重写 save 方法”的语法错误，将默认值逻辑整合到新增接口中
     */
    @HoneyLogs(operation = "订单", type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody Orders orders) {
        User currentUser = TokenUtils.getCurrentUser();
        orders.setUserid(currentUser.getId());
        orders.setDate(DateUtil.today());
        orders.setNo(IdUtil.fastSimpleUUID());
        ordersService.save(orders);
        return Result.success();
    }

    /**
     * 修改用户信息
     * 修复：新增“用户存在性校验”，避免修改不存在的用户
     */
    @HoneyLogs(operation = "订单", type = LogType.UPDATE)

    @PutMapping("/update")
    public Result update(@RequestBody Orders orders) {
       ordersService.updateById(orders);
       return Result.success();
    }
    @HoneyLogs(operation = "订单", type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        ordersService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     */
    @HoneyLogs(operation = "订单", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        ordersService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询所有用户（按ID升序）
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Orders> orderList = ordersService.list(
                new QueryWrapper<Orders>().orderByAsc("id")
        );
        return Result.success(orderList);
    }


    /**
     * 按ID查询单个用户
     */
    @GetMapping("/selectByID/{id}")
    public Result selectByID(@PathVariable Integer id) {
        Orders orders = ordersService.getById(id);
        User user = userService.getById(orders.getUserid());
        if(user!=null){
            orders.setUser(user.getName());
        }
        return Result.success(orders);
    }

    /**
     * 分页查询用户（支持用户名、姓名模糊搜索）
     * 修复：参数非空校验，避免空指针异常
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,  // 默认第1页
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, // 默认每页10条
            @RequestParam(required = false) String name  // 非必填：用户名搜索
    ) {
        // 1. 构建查询条件（模糊搜索+按ID升序）
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>()
                .orderByAsc("id")
                .like(StrUtil.isNotBlank(name), "title", name);             // 姓名非空才模糊搜索

        // 2. 调用 Service 分页查询（MyBatis-Plus 自带的 page 方法）
        Page<Orders> newsPage = ordersService.page(
                new Page<>(pageNum, pageSize),  // 分页参数
                queryWrapper                    // 查询条件
        );
        List<Orders> records = newsPage.getRecords();
        for (Orders record : records) {
            Integer authorid = record.getUserid();
            User user = userService.getById(authorid);
            if(user!=null){
                record.setUser(user.getName());
            }
        }

        // 3. 返回分页结果（包含总条数、当前页数据等）
        return Result.success(newsPage);
    }
}