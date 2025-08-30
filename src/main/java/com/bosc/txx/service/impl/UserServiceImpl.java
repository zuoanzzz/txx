package com.bosc.txx.service.impl;

import com.bosc.txx.model.User;
import com.bosc.txx.dao.UserMapper;
import com.bosc.txx.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * <p>
 * 现实员工表 服务实现类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public User loginByEmployeeNoAndPassword(String employeeNo, String password) {
        if (employeeNo == null || password == null) {
            return null;
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmployeeNo, employeeNo);
        User user = this.getOne(wrapper, false);
        if (user == null || user.getPassword() == null) {
            return null;
        }

        String rawMd5 = md5Hex(password);
        if (user.getPassword().equalsIgnoreCase(rawMd5)) {
            return user;
        }

        return null;
    }

    private String md5Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available", e);
        }
    }

    @Override
    public boolean changePassword(User user, String newRawPassword) {
        String newPwdEnc = md5Hex(newRawPassword);
        user.setPassword(newPwdEnc);
        user.setLastLogin(LocalDateTime.now());
        return this.updateById(user);
    }
}
