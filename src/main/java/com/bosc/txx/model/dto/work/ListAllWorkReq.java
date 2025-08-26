package com.bosc.txx.model.dto.work;

import lombok.Data;

/**
 * @author zhoulei
 * @date 2025/8/26
 */

@Data
public class ListAllWorkReq {

    private Long accountId;

    private Long activityId;

    private Long freeCredit;

}
