package com.bosc.txx.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return String.valueOf(obj);
        }
    }

    private static String buildQueryString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> names = request.getParameterNames();
        boolean first = true;
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (!first) sb.append('&');
            sb.append(name).append('=').append(maskIfSensitive(name, request.getParameter(name)));
            first = false;
        }
        return sb.toString();
    }

    private static String maskIfSensitive(String name, String value) {
        if (name == null) return value;
        String lower = name.toLowerCase();
        if (lower.contains("password") || lower.contains("pwd")) {
            return "****";
        }
        return value;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("_log_start_time", System.currentTimeMillis());
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String query = buildQueryString(request);
        String handlerName = handler instanceof HandlerMethod hm ?
                hm.getBeanType().getSimpleName() + "." + hm.getMethod().getName() : String.valueOf(handler);
        log.info("[HTTP] -> method={}, uri={}, handler={}, query={}", method, uri, handlerName, query);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long start = (Long) request.getAttribute("_log_start_time");
        long cost = start == null ? -1 : (System.currentTimeMillis() - start);
        String handlerName = handler instanceof HandlerMethod hm ?
                hm.getBeanType().getSimpleName() + "." + hm.getMethod().getName() : String.valueOf(handler);
        int status = response.getStatus();
        if (ex == null) {
            log.info("[HTTP] <- handler={}, status={}, costMs={}", handlerName, status, cost);
        } else {
            log.error("[HTTP] <- handler={}, status={}, costMs={}, ex={}", handlerName, status, cost, ex.toString());
        }
    }
}



