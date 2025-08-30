package com.bosc.txx.vo.benefitcode;

import lombok.Data;

@Data
public class ListUserBenefitCodeVO {
    private Long userId;
    private Integer pageNum;
    private Integer pageSize;
}
