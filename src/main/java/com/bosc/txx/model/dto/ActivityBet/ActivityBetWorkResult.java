package com.bosc.txx.model.dto.activitybet;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhoulei
 * @date 2025/9/8
 */

@Data
public class ActivityBetWorkResult {

    private String activityName;

    private String workName;

    private Long betAmount;

}
