package com.example.springboots.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboots.common.AuthAccess;
import com.example.springboots.common.Result;
import com.example.springboots.entity.Logs;
import com.example.springboots.entity.Notice;
import com.example.springboots.entity.User;
import com.example.springboots.service.LogsService;
import com.example.springboots.service.NoticeService;
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
@RequestMapping("/logs")
public class LogsController {

    // 注入用户服务层（MyBatis-Plus 生成的 Service）
    @Autowired
    private LogsService logsService;

    @Autowired
    UserService userService;


    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        logsService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        logsService.removeBatchByIds(ids);
        return Result.success();
    }


    /**
     * 分页查询用户（支持用户名、姓名模糊搜索）
     * 修复：参数非空校验，避免空指针异常
     */
    @AuthAccess
    @GetMapping("/selectByPage")
    public Result selectByPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,  // 默认第1页
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, // 默认每页10条
            @RequestParam(required = false) String operation,  // 非必填：用户名搜索
            @RequestParam(required = false) String type  // 非必填：用户名搜索
    ) {
        // 1. 构建查询条件（模糊搜索+按ID升序）
        QueryWrapper<Logs> queryWrapper = new QueryWrapper<Logs>()
                .orderByAsc("id")
                .like(StrUtil.isNotBlank(operation), "operation", operation)           // 姓名非空才模糊搜索
                .like(StrUtil.isNotBlank(type), "type", type);             // 姓名非空才模糊搜索

        // 2. 调用 Service 分页查询（MyBatis-Plus 自带的 page 方法）
        Page<Logs> page = logsService.page(
                new Page<>(pageNum, pageSize),  // 分页参数
                queryWrapper                    // 查询条件
        );
        return Result.success(page);
    }
}