package com.bosc.txx.vo.benefitcode;

import lombok.Data;

import java.util.List;

@Data
public class BenefitCodeCheckVO {
    private List<String> codes;      // 兑换码列表
    private String redeemedBy;       // 核销管理员ID
}
