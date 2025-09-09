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
 * 权益商品表
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
@TableName("benefit")
public class Benefit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权益名称
     */
    @TableField("name")
    private String name;

    /**
     * 权益账户
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 权益描述
     */
    @TableField("description")
    private String description;

    /**
     * 所需币数
     */
    @TableField("price")
    private Long price;

    /**
     * 图片资源ID
     */
    @TableField("image")
    private String image;

    /**
     * 总数量
     */
    @TableField("total")
    private Integer total;

    /**
     * 剩余数量
     */
    @TableField("remain")
    private Integer remain;

    /**
     * 过期时间
     */
    @TableField("exp_date")
    private LocalDateTime expDate;

    /**
     * 是否启用
     */
    @TableField("active")
    private Boolean active;

    /**
     * 是否删除
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
