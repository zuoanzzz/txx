package com.bosc.txx.model.dto.account;

import lombok.Data;

@Data
public class TransferDTO {
    private String sourceAccountId;
    private String sourceAccountType;
    private String sourceName;
    private String targetAccountId;
    private String targetAccountType;
    private String targetName;
    private Long amount;
    private String reason;

    private Long createdBy; //调用人userId
}
