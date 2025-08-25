package com.bosc.txx.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 现实员工表
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工号
     */
    @TableField("employee_no")
    private String employeeNo;

    /**
     * 密码（加密存储）
     */
    @TableField("password")
    private String password;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 部门
     */
    @TableField("department")
    private String department;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 角色
     */
    @TableField("role")
    private String role;

    /**
     * 是否删除
     */
    @TableField("deleted")
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 最后登录时间
     */
    @TableField("last_login")
    private LocalDateTime lastLogin;
}
