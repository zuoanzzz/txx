package com.bosc.txx.model.dto.account;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoDTO {
    // User信息的
    private String employeeNo;
    private String name;
    private String department;
    private String email;
    private String phone;
    private String role;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private LocalDateTime lastLogin;

    // account信息
    private String accountId;
    private String accountType;
    private Long balance;
}
