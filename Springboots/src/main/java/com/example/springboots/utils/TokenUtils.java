package com.example.springboots.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboots.entity.User;
import com.example.springboots.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;

@Component
public class TokenUtils {

    private static UserMapper staticUserMapper;

    @Resource
    private UserMapper userMapper;

    @PostConstruct
    public void setUserMapper() {
        staticUserMapper = this.userMapper;
    }

    // 生成token
    public static String createToken(String userId, String sign) {
        // 增加参数校验
        if (StrUtil.isBlank(userId) || StrUtil.isBlank(sign)) {
            throw new IllegalArgumentException("用户ID和签名不能为空");
        }
        return JWT.create()
                .withAudience(userId)  // 将userId存储到token中
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))  // 2小时后过期
                .sign(Algorithm.HMAC256(sign));  // 使用密码作为签名
    }

    // 获取当前用户
    public static User getCurrentUser() {
        try {
            // 获取当前请求的HttpServletRequest对象
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return null;  // 非Web环境下返回null
            }
            HttpServletRequest request = requestAttributes.getRequest();
            String token = request.getHeader("token");

            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                // MyBatis-Plus标准查询方法：selectById
                return staticUserMapper.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            // 可以根据需要记录日志
            // log.error("获取当前用户失败", e);
            return null;
        }
        return null;
    }
}