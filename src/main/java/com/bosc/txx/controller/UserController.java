package com.bosc.txx.controller;

import com.bosc.txx.model.User;
import com.bosc.txx.model.dto.login.LoginRequest;
import com.bosc.txx.model.dto.login.LoginResponse;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.dto.login.ChangePasswordRequest;
import com.bosc.txx.service.IUserService;
import com.bosc.txx.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.bosc.txx.enums.CodeEnum.FIRST_LOGIN;

/**
 * <p>
 * 现实员工表 前端控制器
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    
    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public CommonResult<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest) {
        User user = userService.loginByEmployeeNoAndPassword(loginRequest.getEmployeeNo(), loginRequest.getPassword());
        if (user == null) {
            return CommonResult.failed();
        }
        
        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmployeeNo(), user.getRole());
        
        if (Objects.isNull(user.getLastLogin())) {
            LoginResponse loginResponse = new LoginResponse(token, user, 86400000L); // 24小时
            return CommonResult.success(FIRST_LOGIN.getCode(), loginResponse, FIRST_LOGIN.getDescription());
        }
        
        user.setLastLogin(LocalDateTime.now());
        boolean ok = userService.updateById(user);
        if (ok) {
            LoginResponse loginResponse = new LoginResponse(token, user, 86400000L); // 24小时
            return CommonResult.success(loginResponse);
        } else {
            return CommonResult.failed();
        }
    }

    @PostMapping("/changePassword")
    public CommonResult<Boolean> changePassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        if (userId == null) {
            return CommonResult.failed();
        }
        
        User user = userService.getById(userId);
        if (user == null) {
            return CommonResult.failed();
        }
        
        boolean ok = userService.changePassword(user, request.getNewPassword());
        return ok ? CommonResult.success(true) : CommonResult.failed();
    }
}
