package com.bosc.txx.model.dto.work;

import lombok.Data;

import java.util.List;

/**
 * @author zhoulei
 * @date 2025/8/26
 */

@Data
public class ListAllWorkResp {

    private List<WorkDto> workList;

    private Long FreeCredit;

}
