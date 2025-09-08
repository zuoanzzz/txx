package com.bosc.txx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.Activity;
import com.bosc.txx.service.IActivityService;
import com.bosc.txx.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;

import static com.bosc.txx.enums.CodeEnum.*;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private IActivityService activityService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public CommonResult<Activity> create(@RequestBody Activity activity, HttpServletRequest request) {
        if (activity.getName() == null || activity.getName().isEmpty()) {
            return CommonResult.success(ACTIVITY_NAME_EMPTY.getCode(), null, ACTIVITY_NAME_EMPTY.getDescription());
        }

        Activity exist = activityService.getByName(activity.getName());
        if (Objects.nonNull(exist)) {
            return CommonResult.success(ACTIVITY_NAME_DUPLICATED.getCode(), null, ACTIVITY_NAME_DUPLICATED.getDescription());
        }

        String token = jwtUtil.extractTokenFromHeader(request.getHeader("Authorization"));
        boolean ok = activityService.createActivityAccount(activity, jwtUtil.getUserIdFromToken(token));
        return ok ? CommonResult.success() : CommonResult.failed();
    }

    @PutMapping("/update")
    public CommonResult<Activity> update(@RequestBody Activity activity) {
        Activity exist = activityService.getById(activity.getId());
        if (Objects.isNull(exist)) {
            return CommonResult.success(ACTIVITY_NO_EXISTS.getCode(), null, ACTIVITY_NO_EXISTS.getDescription());
        }

        boolean ok = activityService.updateById(activity);
        return ok ? CommonResult.success() : CommonResult.failed();
    }

    @DeleteMapping("/delete/{id}")
    public CommonResult<Activity> delete(@PathVariable Long id) {
        Activity exist = activityService.getById(id);
        if (Objects.isNull(exist)) {
            return CommonResult.success(ACTIVITY_NO_EXISTS.getCode(), null, ACTIVITY_NO_EXISTS.getDescription());
        }

        boolean ok = activityService.removeById(id);
        return ok ? CommonResult.success() : CommonResult.failed();
    }

    @GetMapping("/selectById/{id}")
    public CommonResult<Activity> selectById(@PathVariable Long id) {
        Activity exist = activityService.getById(id);
        return CommonResult.success(exist);
    }

    @GetMapping("/listAll")
    public CommonResult<List<Activity>> listAll(@RequestParam(required = false) String name) {
        List<Activity> list = activityService.list();
        return CommonResult.success(list);
    }

    @GetMapping("/page")
    public CommonResult<Page<Activity>> page(@RequestParam long pageNum, @RequestParam long pageSize) {
        Page<Activity> page = new Page<>(pageNum, pageSize);
        Page<Activity> result = activityService.page(page);
        // 计算并填充分页记录的状态
        LocalDateTime now = LocalDateTime.now();
        result.getRecords().forEach(a -> {
            if (a == null) return;
            LocalDateTime start = a.getStartTime();
            LocalDateTime end = a.getEndTime();
            if (start != null && now.isBefore(start)) {
                a.setStatus(0);
            } else if (end != null && now.isAfter(end)) {
                a.setStatus(2);
            } else {
                a.setStatus(1);
            }
        });
        return CommonResult.success(result);
    }

    @GetMapping("/export-excel/{id}")
    public void exportExcel(@PathVariable Long id, HttpServletResponse response) throws IOException {
        // 设置响应内容类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=multi-sheet-data.xlsx");

        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // 创建第一个Sheet：用户投注信息明细
        Sheet userSheet = workbook.createSheet("用户投注信息明细");
        activityService.createUserSheet(id, userSheet, headerStyle);

//        // 创建第二个Sheet：作品投注金额概览
//        Sheet workSheet = workbook.createSheet("作品投注金额概览");
//        activityService.createWorkSheet(id, workSheet, headerStyle);

        // 写入响应输出流
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
