package com.bosc.txx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 交易流水表
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
@TableName("transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流水号
     */
    @TableField("tx_no")
    private String txNo;

    /**
     * 源账户ID
     */
    @TableField("source_account_id")
    private Long sourceAccountId;

    /**
     * 目标账户ID
     */
    @TableField("target_account_id")
    private Long targetAccountId;

    /**
     * 源账户用户名
     */
    @TableField("source_name")
    private String sourceName;

    /**
     * 目标账户用户名
     */
    @TableField("target_name")
    private String targetName;

    /**
     * 源账户类型
     */
    @TableField("source_account_type")
    private String sourceAccountType;

    /**
     * 目标账户类型
     */
    @TableField("target_account_type")
    private String targetAccountType;

    /**
     * 交易金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 交易类型
     */
    @TableField("tx_type")
    private String txType;

    /**
     * 事由说明
     */
    @TableField("reason")
    private String reason;

    /**
     * 操作人用户ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 关联投注ID
     */
    @TableField("related_bet_id")
    private Long relatedBetId;

    /**
     * 冗余字段
     */
    @TableField("metadata")
    private String metadata;

    /**
     * 交易发起时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 交易完成时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;
}
