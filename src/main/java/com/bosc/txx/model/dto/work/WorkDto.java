package com.bosc.txx.model.dto.work;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhoulei
 * @date 2025/8/26
 */

@Data
public class WorkDto {

    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 作品标题
     */
    private String title;

    /**
     * 投注总金额
     */
    private Long amount;

    /**
     * 作者
     */
    private String authors;

    /**
     * 作品描述
     */
    private String description;

    /**
     * 封面
     */
    private String cover;

    /**
     * 作品详情云文档URL
     */
    private String link;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

}
