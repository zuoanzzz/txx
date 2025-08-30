package com.bosc.txx.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class ControllerLogAspect {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Pointcut("execution(public * com.bosc.txx.controller..*.*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();

        String paramsJson = safeToJson(maskSensitiveArgs(signature.getParameterNames(), joinPoint.getArgs()));
        log.info("[{}.{}] 入参: {}", className, methodName, paramsJson);

        Object result = null;
        Throwable ex = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            ex = e;
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - start;
            if (ex == null) {
                log.info("[{}.{}] 出参: {}, 耗时: {}ms", className, methodName, truncate(safeToJson(result), 2000), cost);
            } else {
                log.error("[{}.{}] 异常: {}, 耗时: {}ms", className, methodName, ex.toString(), cost);
            }
        }
    }

    private Map<String, Object> maskSensitiveArgs(String[] names, Object[] args) {
        Map<String, Object> map = new HashMap<>();
        if (names == null || args == null) return map;
        for (int i = 0; i < Math.min(names.length, args.length); i++) {
            String name = names[i];
            Object value = args[i];
            if (name != null && (name.toLowerCase().contains("password") || name.toLowerCase().contains("pwd"))) {
                map.put(name, "****");
            } else {
                map.put(name, value);
            }
        }
        return map;
    }

    private String safeToJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return String.valueOf(obj);
        }
    }

    private String truncate(String s, int max) {
        if (s == null) return null;
        if (s.length() <= max) return s;
        return s.substring(0, max) + "...";
    }
}



