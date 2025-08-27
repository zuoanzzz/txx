package com.bosc.txx.vo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BenefitResponseVO {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String image;
    private Integer total;
    private Integer remain;
    private Boolean active;
    private LocalDateTime expDate;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
