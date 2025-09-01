package com.bosc.txx.controller;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.Work;
import com.bosc.txx.model.dto.work.ListAllWorkReq;
import com.bosc.txx.model.dto.work.ListAllWorkResp;
import com.bosc.txx.service.IWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 活动作品表 前端控制器
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@RestController
@RequestMapping("/work")
public class WorkController {

    @Autowired
    private IWorkService workService;

    @PostMapping("/listAll")
    public CommonResult<ListAllWorkResp> listAll(@RequestBody ListAllWorkReq req) {
        ListAllWorkResp listAllWorkResp = workService.listAll(req);
        return CommonResult.success(listAllWorkResp);
    }

    @PostMapping("/create")
    public CommonResult<Work> create(@RequestBody Work work) {
        boolean ok = workService.save(work);
        return ok ? CommonResult.success(work) : CommonResult.failed();
    }

    @PutMapping("/update")
    public CommonResult<Boolean> update(@RequestBody Work work) {
        boolean ok = workService.updateById(work);
        return ok ? CommonResult.success(true) : CommonResult.failed();
    }

    @DeleteMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean ok = workService.removeById(id);
        return ok ? CommonResult.success(true) : CommonResult.failed();
    }

    @GetMapping("/getById/{id}")
    public CommonResult<Work> getById(@PathVariable Long id) {
        Work work = workService.getById(id);
        if(work == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(work);
    }

}
