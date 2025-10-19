package com.example.springboots.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboots.common.HoneyLogs;
import com.example.springboots.common.LogType;
import com.example.springboots.common.Result;
import com.example.springboots.entity.User;
import com.example.springboots.exception.ServiceException;
import com.example.springboots.service.UserService;
import com.example.springboots.utils.TokenUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理控制器
 * 处理用户的增删改查、分页查询等请求
 */
@RestController
@RequestMapping("/user")
public class UserController {

    // 注入用户服务层（MyBatis-Plus 生成的 Service）
    @Autowired
    private UserService userService;

    /**
     * 新增用户（含默认值填充）
     * 解决原代码中“重写 save 方法”的语法错误，将默认值逻辑整合到新增接口中
     */
    @HoneyLogs(operation = "用户", type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        // 1. 校验必填参数（用户名不能为空）
        if (StrUtil.isBlank(user.getUsername())) {
            return Result.error("用户名不能为空");
        }
        // 2. 填充默认值（密码默认123、角色默认“用户”）
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword("123"); // 注意：实际项目需加密存储（如 BCrypt），此处仅为示例
        }
        if (StrUtil.isBlank(user.getRole())) {
            user.setRole("用户");
        }
        User dbUser = userService.selectByUsername(user.getUsername());
        if(dbUser!=null){
            throw new ServiceException("用户名已存在");
        }
        // 3. 调用 Service 保存用户（MyBatis-Plus 自带的 save 方法）
        boolean saveSuccess = userService.save(user);
        if (saveSuccess) {
            return Result.success("用户新增成功");
        } else {
            return Result.error("用户新增失败");
        }
    }

    /**
     * 修改用户信息
     * 修复：新增“用户存在性校验”，避免修改不存在的用户
     */
    @HoneyLogs(operation = "用户", type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        // 1. 校验用户ID（修改必须指定ID）
        if (user.getId() == null) {
            return Result.error("用户ID不能为空");
        }
        // 2. 校验用户是否存在
        User existUser = userService.getById(user.getId());
        if (existUser == null) {
            return Result.error("该用户不存在，无法修改");
        }
        // 3. 调用 Service 更新用户（MyBatis-Plus 自带的 updateById 方法）
        boolean updateSuccess = userService.updateById(user);
        if (updateSuccess) {
            return Result.success("用户修改成功");
        } else {
            return Result.error("用户修改失败");
        }
    }

    /**
     * 单个删除用户
     */
    /**
     * 单个删除用户
     */
    @HoneyLogs(operation = "用户", type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        // 1. 校验ID
        if (id == null) {
            return Result.error("用户ID不能为空");
        }
        // 2. 校验用户是否存在（用 getById 替代 existsById）
        User existUser = userService.getById(id);

        if (existUser == null) {
            return Result.error("该用户不存在，无法删除");
        }
        if(existUser.equals(TokenUtils.getCurrentUser())){
            throw new ServiceException("不能删除当前用户");
        }
        // 3. 调用 Service 删除用户
        boolean deleteSuccess = userService.removeById(id);
        if (deleteSuccess) {
            return Result.success("用户删除成功");
        } else {
            return Result.error("用户删除失败");
        }
    }

    /**
     * 批量删除用户
     */
    @HoneyLogs(operation = "用户", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        // 1. 校验ID列表
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的用户");
        }
        // 2. 调用 Service 批量删除
        User currentUser = TokenUtils.getCurrentUser();
        if(currentUser !=null && ids.contains(currentUser.getId()) && currentUser.getId() != null){
            throw new ServiceException("不能删除当前的用户");
        }
        boolean batchDeleteSuccess = userService.removeBatchByIds(ids);
        if (batchDeleteSuccess) {
            return Result.success("批量删除成功");
        } else {
            return Result.error("批量删除失败");
        }
    }

    /**
     * 查询所有用户（按ID升序）
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> userList = userService.list(
                new QueryWrapper<User>().orderByAsc("id")
        );
        return Result.success(userList);
    }

    /**
     * 按ID查询单个用户
     */
    @GetMapping("/selectByID/{id}")
    public Result selectByID(@PathVariable Integer id) {
        // 1. 校验ID
        if (id == null) {
            return Result.error("用户ID不能为空");
        }
        // 2. 查询用户
        User user = userService.getById(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("该用户不存在");
        }
    }

    /**
     * 分页查询用户（支持用户名、姓名模糊搜索）
     * 修复：参数非空校验，避免空指针异常
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,  // 默认第1页
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, // 默认每页10条
            @RequestParam(required = false) String username,  // 非必填：用户名搜索
            @RequestParam(required = false) String name       // 非必填：姓名搜索
    ) {
        // 1. 构建查询条件（模糊搜索+按ID升序）
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .orderByAsc("id")
                .like(StrUtil.isNotBlank(username), "username", username) // 用户名非空才模糊搜索
                .like(StrUtil.isNotBlank(name), "name", name);             // 姓名非空才模糊搜索

        // 2. 调用 Service 分页查询（MyBatis-Plus 自带的 page 方法）
        Page<User> userPage = userService.page(
                new Page<>(pageNum, pageSize),  // 分页参数
                queryWrapper                    // 查询条件
        );

        // 3. 返回分页结果（包含总条数、当前页数据等）
        return Result.success(userPage);
    }

    @GetMapping("/export")
    public void exportData( @RequestParam(required = false) String username,  // 非必填：用户名搜索
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) String ids,
                            HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<User> list;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if(StrUtil.isNotBlank(ids)){
            List<Integer> idsArr1 = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id", idsArr1);
        }else{
            //全部导出
            queryWrapper.like(StrUtil.isNotBlank(username), "username", username);//条件查询器
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        }
        list = userService.list(queryWrapper);
        writer.write(list,true);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=user.xlsx" + URLEncoder.encode("用户信息表", "UTF-8") + ".xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream,true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping("import")
    public Result importData( MultipartFile file)throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> userList = reader.readAll(User.class);
        List<String> excelUsernames = userList.stream()
                .map(User::getUsername) // 提取用户名列表
                .collect(Collectors.toList());
        // 构造查询条件：查询数据库中“在 Excel 用户名列表中”的记录
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("username", excelUsernames); // in 条件：匹配多个用户名
        List<User> dbExistUsers = userService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(dbExistUsers)) {
            // 提取冲突的用户名，拼接成错误信息
            String conflictUsernames = dbExistUsers.stream()
                    .map(User::getUsername)
                    .collect(Collectors.joining(","));
            return Result.error("数据库中已存在以下用户名：" + conflictUsernames + "，请修改后重新导入");
        }

        try{
            userService.saveBatch(userList);
        }catch (Exception e){
            return Result.error("导入出错");
        }
        return Result.success();
    }
}