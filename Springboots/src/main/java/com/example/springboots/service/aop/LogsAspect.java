package com.example.springboots.service.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.example.springboots.common.HoneyLogs;
import com.example.springboots.entity.Logs;
import com.example.springboots.entity.User;
import com.example.springboots.service.LogsService;
import com.example.springboots.utils.IpUtils;
import com.example.springboots.utils.TokenUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
@Slf4j
public class LogsAspect {
    @Resource
    LogsService logsService;  // 修正：原代码中变量名首字母大写，建议改为小写（符合命名规范）

    @AfterReturning(pointcut = "@annotation(honeyLogs)", returning = "jsonResult")
    public void recordLog(JoinPoint joinPoint, HoneyLogs honeyLogs, Object jsonResult) {
        // 1. 获取当前操作用户信息
        User loginUser = TokenUtils.getCurrentUser();
        if (loginUser == null) { // 未登录时，尝试从方法参数中获取用户（如登录/注册场景）
            Object[] args = joinPoint.getArgs();
            if (ArrayUtil.isNotEmpty(args) && args[0] instanceof User) {
                loginUser = (User) args[0];
            }
        }
        if (loginUser == null) { // 仍未获取到用户，记录错误并返回
            log.error("记录日志失败：未获取到操作用户信息");
            return;
        }

        // 2. 获取请求相关信息（IP地址等）
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        jakarta.servlet.http.HttpServletRequest request = requestAttributes.getRequest();
        String ipAddr = IpUtils.getIpAddr(request); // 获取客户端IP

        // 3. 构建日志对象
        Logs logs = Logs.builder()
                .user(loginUser.getUsername()) // 操作人用户名
                .operation(honeyLogs.operation()) // 从注解获取操作名称
                .type(honeyLogs.type().getValue()) // 从注解获取操作类型（假设type是枚举，调用getValue()获取值）
                .ip(ipAddr) // 客户端IP
                .time(DateUtil.now()) // 当前时间（Hutool工具类）
                .build();

        // 4. 异步保存日志（不阻塞主流程）
        System.out.println(logs);
// 修正后的异步保存逻辑
        ThreadUtil.execAsync(() -> {
            try {
                // 打印日志对象，确认数据是否正常（排查字段为空问题）
                log.info("准备保存日志：{}", logs);
                logsService.save(logs);
                log.info("日志保存成功：{}", logs);
            } catch (Exception e) {
                // 捕获所有异常并打印详细堆栈，定位问题
                log.error("异步保存日志失败！日志内容：{}", logs, e);
            }
        });
    }
}