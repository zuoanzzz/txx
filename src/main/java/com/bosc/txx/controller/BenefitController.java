package com.bosc.txx.controller;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.Benefit;
import com.bosc.txx.service.IBenefitService;
import com.bosc.txx.util.JwtUtil;
import com.bosc.txx.vo.benefit.BenefitCreateVO;
import com.bosc.txx.vo.benefit.GetAllBenefitVO;
import com.bosc.txx.vo.benefit.ListAllBenefitVO;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/listAll")
    public CommonResult<List<Benefit>> listAll(@RequestBody ListAllBenefitVO request) {
        if (request.getPageNum() == null) {
            request.setPageNum(1);
        }
        if (request.getPageSize() == null) {
            request.setPageSize(10);
        }

        return benefitService.listAllBenefits(request);
    }

    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody BenefitCreateVO vo, HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromHeader(request.getHeader("Authorization"));
        vo.setCreatedBy(jwtUtil.getUserIdFromToken(token));
        return benefitService.createBenefit(vo);
    }

    @PostMapping("/update")
    public CommonResult<?> update(@RequestBody BenefitCreateVO vo) {
        return benefitService.updateBenefit(vo);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResult<?> delete(@PathVariable Long id) {
        return benefitService.deleteBenefit(id);
    }

    // 管理员调用，展示已过期的权益
    @PostMapping("/getAllBenefit")
    public CommonResult<List<Benefit>> getAllBenefit(@RequestBody GetAllBenefitVO request) {
        if (request.getPageNum() == null) {
            request.setPageNum(1);
        }
        if (request.getPageSize() == null) {
            request.setPageSize(10);
        }

        return benefitService.getAllBenefit(request);
    }
}
