package com.bosc.txx.model.dto.ActivityBet;

import com.bosc.txx.model.User;
import lombok.Data;

/**
 * @author zhoulei
 * @date 2025/8/31
 */

@Data
public class ActivityBetReq {

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 作品ID
     */
    private Long workId;

    /**
     * 投注金额
     */
    private Long amount;

    /**
     * 使用的免费额度
     */
    private Long usedFreeAmount;

}
