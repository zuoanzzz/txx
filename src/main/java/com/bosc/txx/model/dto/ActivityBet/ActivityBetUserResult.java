package com.bosc.txx.model.dto.activitybet;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhoulei
 * @date 2025/9/8
 */

@Data
public class ActivityBetUserResult {

    private String activityName;

    private String workName;

    private String betName;

    private String employeeNo;

    private Long betAmount;

}
