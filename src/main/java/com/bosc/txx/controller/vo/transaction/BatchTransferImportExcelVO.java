package com.bosc.txx.controller.vo.transaction;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class BatchTransferImportExcelVO {

    /**
     * 工号
     */
    @ExcelProperty("工号")
    private String employeeNo;

    /**
     * 发放金额
     */
    @ExcelProperty("发放金额")
    private Integer amount;

    /**
     * 事由（备注）
     */
    @ExcelProperty("事由（备注）")
    private String remark;

}
