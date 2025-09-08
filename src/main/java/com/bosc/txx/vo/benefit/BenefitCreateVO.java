package com.bosc.txx.vo.benefit;

import lombok.Data;

@Data
public class BenefitCreateVO {
    private Long id; // update 时需传入 id；create 时可不传
    private String name;
    private String description;
    private Long price;
    private String image;
    private Integer total;
    private Integer remain;
    private Boolean active;
    private String expDate;

    private Long createdBy;
}
