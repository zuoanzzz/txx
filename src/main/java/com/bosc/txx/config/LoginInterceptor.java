package com.bosc.txx.config;

import com.bosc.txx.common.CommonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * 登录拦截器
 *
 * @author zhoulei
 * @date 2025/8/25
 */

//@Component
//public class LoginInterceptor implements HandlerInterceptor {
//
//    private static final String LOGIN_SESSION_KEY = "LOGIN_USER";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Object loginUser = request.getSession().getAttribute(LOGIN_SESSION_KEY);
//        if (loginUser != null) {
//            return true;
//        }
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        response.setContentType("application/json;charset=UTF-8");
//        String body = new ObjectMapper().writeValueAsString(CommonResult.failed());
//        response.getWriter().write(body);
//        return false;
//    }
//
//    public static String getLoginSessionKey() {
//        return LOGIN_SESSION_KEY;
//    }
//}


