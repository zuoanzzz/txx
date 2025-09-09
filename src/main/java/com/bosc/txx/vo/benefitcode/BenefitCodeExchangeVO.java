package com.bosc.txx.vo.benefitcode;

import lombok.Data;

@Data
public class BenefitCodeExchangeVO {
    private Long accountId;
    private Long userId;
    private String benefitId;
    private String count;
}
