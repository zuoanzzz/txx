package com.bosc.txx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.Activity;
import com.bosc.txx.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public CommonResult<Activity> create(@RequestBody Activity activity) {
        if (activity.getName() == null || activity.getName().isEmpty()) {
            return CommonResult.success(ACTIVITY_NAME_EMPTY.getCode(), null, ACTIVITY_NAME_EMPTY.getDescription());
        }

        Activity exist = activityService.getByName(activity.getName());
        if (Objects.nonNull(exist)) {
            return CommonResult.success(ACTIVITY_NAME_DUPLICATED.getCode(), null, ACTIVITY_NAME_DUPLICATED.getDescription());
        }

        boolean ok = activityService.save(activity);
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
}
