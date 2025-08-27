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
 * 活动投注记录表
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
@TableName("activity_bet")
public class ActivityBet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    @TableField("activity_id")
    private Long activityId;

    /**
     * 作品ID
     */
    @TableField("work_id")
    private Long workId;

    /**
     * 投注账户ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 投注金额
     */
    @TableField("amount")
    private Long amount;

    /**
     * 使用的免费额度
     */
    @TableField("used_free_amount")
    private Long usedFreeAmount;

    /**
     * 关联交易ID
     */
    @TableField("related_tx_id")
    private Long relatedTxId;

    /**
     * 投注时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;
}
