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
 * 活动作品表
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Getter
@Setter
@ToString
@TableName("work")
public class Work implements Serializable {

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
     * 作品标题
     */
    @TableField("title")
    private String title;

    /**
     * 作者
     */
    @TableField("authors")
    private String authors;

    /**
     * 作品描述
     */
    @TableField("description")
    private String description;

    /**
     * 封面
     */
    @TableField("cover")
    private String cover;

    /**
     * 作品详情云文档URL
     */
    @TableField("link")
    private String link;

    /**
     * 是否删除
     */
    @TableField("deleted")
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
