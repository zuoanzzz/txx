package com.bosc.txx.model.dto.benefitcode;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListAllBenefitCodeDTO {
    private String accountId;
    private Long benefitId;
    private String benefitName;
    private Integer status;
    private String code;
    private LocalDateTime expDate;
}
