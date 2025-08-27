package com.bosc.txx.vo;

import lombok.Data;

@Data
public class AccountCreateVO {
    private String userId;
    private String employeeNo;  // 工号
    private String name;        // 姓名
    private String department;  // 部门
}
