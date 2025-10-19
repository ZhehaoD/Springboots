package com.example.springboots.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboots.common.AuthAccess;
import com.example.springboots.common.HoneyLogs;
import com.example.springboots.common.LogType;
import com.example.springboots.common.Result;
import com.example.springboots.entity.Notice;
import com.example.springboots.entity.User;
import com.example.springboots.service.NewsService;
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
@RequestMapping("/notice")
public class NoticeController {

    // 注入用户服务层（MyBatis-Plus 生成的 Service）
    @Autowired
    private NoticeService noticeService;

    @Autowired
    UserService userService;
    /**
     * 新增用户（含默认值填充）
     * 解决原代码中“重写 save 方法”的语法错误，将默认值逻辑整合到新增接口中
     */
    @HoneyLogs(operation = "公告", type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        User currentUser = TokenUtils.getCurrentUser();
        notice.setUserid(currentUser.getId());
        notice.setTime(DateUtil.now());
        noticeService.save(notice);
        return Result.success();
    }

    /**
     * 修改用户信息
     * 修复：新增“用户存在性校验”，避免修改不存在的用户
     */
    @HoneyLogs(operation = "公告", type = LogType.UPDATE)

    @PutMapping("/update")
    public Result update(@RequestBody Notice notice) {
       noticeService.updateById(notice);
       return Result.success();
    }
    @HoneyLogs(operation = "公告", type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        noticeService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     */
    @HoneyLogs(operation = "公告", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        noticeService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询所有用户（按ID升序）
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Notice> newsList = noticeService.list(
                new QueryWrapper<Notice>().orderByAsc("id")
        );
        return Result.success(newsList);
    }

    @GetMapping("/selectUserData")
    public Result selectUserData() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByDesc("id");
        queryWrapper.eq("open", true);
        List<Notice> newsList = noticeService.list(queryWrapper);
        return Result.success(newsList);
    }

    /**
     * 按ID查询单个用户
     */
    @GetMapping("/selectByID/{id}")
    public Result selectByID(@PathVariable Integer id) {
        Notice notice = noticeService.getById(id);
        User user = userService.getById(notice.getUserid());
        if(user!=null){
            notice.setUserid(user.getId());
        }
        return Result.success(notice);
    }

    /**
     * 分页查询用户（支持用户名、姓名模糊搜索）
     * 修复：参数非空校验，避免空指针异常
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,  // 默认第1页
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, // 默认每页10条
            @RequestParam(required = false) String title  // 非必填：用户名搜索
    ) {
        // 1. 构建查询条件（模糊搜索+按ID升序）
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>()
                .orderByAsc("id")
                .like(StrUtil.isNotBlank(title), "title", title);             // 姓名非空才模糊搜索

        // 2. 调用 Service 分页查询（MyBatis-Plus 自带的 page 方法）
        Page<Notice> newsPage = noticeService.page(
                new Page<>(pageNum, pageSize),  // 分页参数
                queryWrapper                    // 查询条件
        );
        List<Notice> records = newsPage.getRecords();
        for (Notice record : records) {
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