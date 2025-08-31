package com.bosc.txx.vo.benefitcode;

import lombok.Data;

@Data
public class BenefitCodeExchangeVO {
    private String accountId;
    private Long userId;
    private String benefitId;
    private String count;
}
