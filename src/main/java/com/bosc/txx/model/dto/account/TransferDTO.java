package com.bosc.txx.model.dto.account;

import lombok.Data;

@Data
public class TransferDTO {
    private Long sourceAccountId;
    private String sourceAccountType;
    private String sourceName;
    private Long targetAccountId;
    private String targetAccountType;
    private String targetName;
    private Long amount;
    private Long usedFreeAmount;
    private String reason;

    private Long createdBy; //调用人userId
}
