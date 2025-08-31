package com.bosc.txx.vo.account;

import lombok.Data;

@Data
public class TransferVO {
    private String targetAccountId;
    private Long amount;
    private String reason;

    private Long createdBy; //调用人userId
}
