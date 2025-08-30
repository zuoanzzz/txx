package com.bosc.txx.model.dto.benefitcode;

import lombok.Data;

@Data
public class ListAllBenefitCodeDTO {
    private String accountId;
    private String benefitId;
    private String benefitName;
    private Boolean status;
    private String code;
}
