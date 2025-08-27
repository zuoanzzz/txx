package com.bosc.txx.controller;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.Benefit;
import com.bosc.txx.service.IBenefitService;
import com.bosc.txx.vo.BenefitCreateVO;
import com.bosc.txx.vo.BenefitResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权益商品表 前端控制器
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@RestController
@RequestMapping("/benefit")
public class BenefitController {
    @Autowired
    private IBenefitService benefitService;

    @GetMapping("/listAll")
    public CommonResult<List<Benefit>> listAll() {
        return benefitService.listAllBenefits();
    }

    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody BenefitCreateVO vo) {
        return benefitService.createBenefit(vo);
    }

    @PutMapping("/update/{id}")
    public CommonResult<?> update(@RequestBody BenefitCreateVO vo) {
        return benefitService.updateBenefit(vo);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResult<?> delete(@PathVariable Long id) {
        return benefitService.deleteBenefit(id);
    }
}
