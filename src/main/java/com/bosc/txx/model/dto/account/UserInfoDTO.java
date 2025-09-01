package com.bosc.txx.model.dto.account;

import lombok.Data;

@Data
public class UserInfoDTO {
    // User信息的
    private String employeeNo;
    private String name;
    private String department;
    private String email;
    private String phone;
    private String role;

    // account信息
    private String accountId;
    private String accountType;
    private Long balance;
}
