package com.bosc.txx.vo.benefitcode;

import lombok.Data;

@Data
public class queryByUserNameVO {
    private String userName;
    private Integer pageNum;
    private Integer pageSize;
}
