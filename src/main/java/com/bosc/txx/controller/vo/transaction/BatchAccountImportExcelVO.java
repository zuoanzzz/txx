package com.bosc.txx.controller.vo.transaction;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author zhoulei
 * @date 2025/8/31
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false)
public class BatchAccountImportExcelVO {

    @ExcelProperty("工号")
    private String employeeNo;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("部门")
    private String department;

}
