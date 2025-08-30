package com.bosc.txx.vo.account;

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

    private String userId;
}
