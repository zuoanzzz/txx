package com.bosc.txx.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统账户表
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
@TableName("account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 余额账号名
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 关联用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 账户类型
     */
    @TableField("account_type")
    private String accountType;

    /**
     * 余额
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 是否启用
     */
    @TableField("deleted")
    private Boolean deleted;

    /**
     * 创建者ID
     */
    @TableField("created_by")
    private Long createdBy;

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
}
