package com.bosc.txx.controller;

import com.bosc.txx.model.User;
import com.bosc.txx.model.dto.login.LoginRequest;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.dto.login.ChangePasswordRequest;
import com.bosc.txx.service.IUserService;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/login")
    public CommonResult<User> login(@RequestBody LoginRequest request, HttpSession session) {
        User user = userService.loginByEmployeeNoAndPassword(request.getEmployeeNo(), request.getPassword());
        if (user == null) {
            return CommonResult.failed();
        }
        session.setAttribute("LOGIN_USER", user);
        if (Objects.isNull(user.getLastLogin())) {
            return CommonResult.success(FIRST_LOGIN.getCode(), user, FIRST_LOGIN.getDescription());
        }
        user.setLastLogin(LocalDateTime.now());
        boolean ok = userService.updateById(user);
        return ok ? CommonResult.success(user) :  CommonResult.failed();
    }

    @PostMapping("/changePassword")
    public CommonResult<Boolean> changePassword(@RequestBody ChangePasswordRequest request, HttpSession session) {
        Object sessionUser = session.getAttribute("LOGIN_USER");
        if (!(sessionUser instanceof User u)) {
            return CommonResult.failed();
        }
        boolean ok = userService.changePassword(u, request.getNewPassword());
        return ok ? CommonResult.success(true) : CommonResult.failed();
    }
}
