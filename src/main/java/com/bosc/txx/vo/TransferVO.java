package com.bosc.txx.vo;

import lombok.Data;

@Data
public class TransferVO {
    private String sourceAccountId;
    private String sourceAccountType;
    private String sourceName;
    private String targetAccountId;
    private String targetAccountType;
    private String targetName;
    private String amount;
    private String reason;
}
