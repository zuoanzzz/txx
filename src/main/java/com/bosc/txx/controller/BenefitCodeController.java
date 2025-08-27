package com.bosc.txx.controller;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.BenefitCode;
import com.bosc.txx.service.IBenefitCodeService;
import com.bosc.txx.vo.BenefitCodeCheckVO;
import com.bosc.txx.vo.BenefitCodeExchangeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 兑换码表 前端控制器
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@RestController
@RequestMapping("/benefitCode")
public class BenefitCodeController {

    @Autowired
    IBenefitCodeService iBenefitCodeService;
    /**
     * 兑换权益、生成兑换码
     */
    @PostMapping("/getBenefitCodes")
    public CommonResult<?> getBenefitCodes(@RequestBody BenefitCodeExchangeVO request) {
        return iBenefitCodeService.getBenefitCodes(request);
    }

    /**
     * 查询所有兑换码
     */
    @GetMapping("/listAll")
    public CommonResult<List<Object>> listAll() {
        return iBenefitCodeService.listAll();
    }

    /**
     * 核销兑换码
     */
    @PostMapping("/check")
    public CommonResult<?> check(@RequestBody BenefitCodeCheckVO request) {
        return iBenefitCodeService.check(request);
    }
}
