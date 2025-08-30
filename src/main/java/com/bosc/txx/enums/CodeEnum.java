package com.bosc.txx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhoulei
 * @date 2025/8/25
 */

@Getter
@AllArgsConstructor
public enum CodeEnum {
    SUCCESS(0, "操作成功！"),
    FAILED(10000, "操作失败！"),

    //活动错误码
    ACTIVITY_NAME_EMPTY(20001, "活动名不能为空！"),
    ACTIVITY_NAME_DUPLICATED(20002, "活动名不能重复！"),
    ACTIVITY_NO_EXISTS(20003, "活动不存在！"),

    // 登录相关
    FIRST_LOGIN(30001, "用户首次登录");


    private final Integer code;
    private final String description;
}
