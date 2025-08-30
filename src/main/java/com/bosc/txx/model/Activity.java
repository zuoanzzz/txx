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
 * 活动表
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
@TableName("activity")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    @TableField("name")
    private String name;

    /**
     * 活动描述
     */
    @TableField("description")
    private String description;

    /**
     * 活动账户ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 免费额度
     */
    @TableField("free_credit")
    private Long freeCredit;

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
     * 活动状态（非持久化）：0-未开始；1-进行中；2-已结束
     */
    @TableField(exist = false)
    private Integer status;
}
