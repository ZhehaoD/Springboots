package com.example.springboots.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springboots.entity.User;
import com.example.springboots.exception.ServiceException;
import com.example.springboots.mapper.UserMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 注意：Spring Boot 3+使用jakarta包，Spring Boot 2使用javax包
public class JwtInterceptor implements HandlerInterceptor {

    // 改用@Autowired避免可能的@Resource兼容问题
    @Autowired
    private UserMapper userMapper;

    // 严格匹配接口方法签名
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }

        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("401", "请登录");
        }

        // 获取token中的user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new ServiceException("401", "请登录");
        }

        // 修正方法名大小写（标准MyBatis命名）
        User user = userMapper.selectByID(Integer.valueOf(userId));
        if (user == null) {
            throw new ServiceException("401", "请登录");
        }

        // 验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException("401", "请登录");
        }
        return true;
    }
}
