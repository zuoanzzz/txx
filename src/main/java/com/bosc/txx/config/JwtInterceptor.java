package com.bosc.txx.config;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * JWT认证拦截器
 *
 * @author zhoulei
 * @date 2025/8/27
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 允许OPTIONS预检请求通过
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        // 获取Authorization header
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json;charset=UTF-8");
            String body = new ObjectMapper().writeValueAsString(CommonResult.failed());
            response.getWriter().write(body);
            return false;
        }

        // 提取token
        String token = jwtUtil.extractTokenFromHeader(authHeader);
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json;charset=UTF-8");
            String body = new ObjectMapper().writeValueAsString(CommonResult.failed());
            response.getWriter().write(body);
            return false;
        }

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json;charset=UTF-8");
            String body = new ObjectMapper().writeValueAsString(CommonResult.failed());
            response.getWriter().write(body);
            return false;
        }

        // 将用户信息设置到request属性中，供后续使用
        request.setAttribute("userId", jwtUtil.getUserIdFromToken(token));
        request.setAttribute("employeeNo", jwtUtil.getEmployeeNoFromToken(token));
        request.setAttribute("role", jwtUtil.getRoleFromToken(token));

        return true;
    }
}
