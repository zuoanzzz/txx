package com.bosc.txx.service;

import com.bosc.txx.model.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
public interface IActivityService extends IService<Activity> {

    Activity getByName(String name);

    Boolean createActivityAccount(Activity activity, Long userId);

    void createUserSheet(Long id, Sheet userSheet, CellStyle headerStyle);

//    void createWorkSheet(Long id, Sheet workSheet, CellStyle headerStyle);
}
