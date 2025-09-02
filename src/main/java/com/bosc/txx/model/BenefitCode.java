package com.bosc.txx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 兑换码表
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
@TableName("benefit_code")
public class BenefitCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 对应权益ID
     */
    @TableField("benefit_id")
    private Long benefitId;

    /**
     * 兑换码
     */
    @TableField("code")
    private String code;

    /**
     * 兑换人ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 兑换时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 过期时间
     */
    @TableField("exp_date")
    private LocalDateTime expDate;

    /**
     * 兑换状态
     */
    @TableField("status")
    private String status;

    /**
     * 核销管理员ID
     */
    @TableField("redeemed_by")
    private Long redeemedBy;

    /**
     * 核销时间
     */
    @TableField("redeemed_at")
    private LocalDateTime redeemedAt;

    /**
     * 冗余字段
     */
    @TableField("metadata")
    private String metadata;
}
