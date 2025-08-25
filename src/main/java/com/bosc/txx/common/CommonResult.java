package com.bosc.txx.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import static com.bosc.txx.enums.CodeEnum.FAILED;
import static com.bosc.txx.enums.CodeEnum.SUCCESS;

/**
 * @author zhoulei
 * @date 2025/8/25
 */

@Getter
@Setter
@ToString
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private T data;
    private String message;

    public CommonResult() {}

    public CommonResult(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> CommonResult<T> success() {
        return success(null);
    }

    public static <T> CommonResult<T> success(T data) {
        return success(SUCCESS.getCode(), data, SUCCESS.getDescription());
    }

    public static <T> CommonResult<T> success(Integer code, T data, String message) {
        return new CommonResult<>(code, data, message);
    }

    public static <T> CommonResult<T> failed() {
        return new CommonResult<>(FAILED.getCode(), null, FAILED.getDescription());
    }
}
