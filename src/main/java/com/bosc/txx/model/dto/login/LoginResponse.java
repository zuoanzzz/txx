package com.bosc.txx.model.dto.login;

import com.bosc.txx.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 登录响应DTO
 * 
 * @author zhoulei
 * @date 2025/8/27
 */
@Getter
@Setter
@ToString
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JWT token
     */
    private String token;

    /**
     * 用户信息
     */
    private User user;

    /**
     * token类型
     */
    private String tokenType = "Bearer";

    /**
     * 过期时间（毫秒）
     */
    private Long expiresIn;

    public LoginResponse() {}

    public LoginResponse(String token, User user, Long expiresIn) {
        this.token = token;
        this.user = user;
        this.expiresIn = expiresIn;
    }
}
