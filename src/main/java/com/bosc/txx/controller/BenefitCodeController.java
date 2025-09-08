package com.bosc.txx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.BenefitCode;
import com.bosc.txx.model.dto.benefitcode.ListAllBenefitCodeDTO;
import com.bosc.txx.service.IBenefitCodeService;
import com.bosc.txx.util.JwtUtil;
import com.bosc.txx.vo.benefitcode.*;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private JwtUtil jwtUtil;

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
    @PostMapping("/listAll")
    public CommonResult<List<ListAllBenefitCodeDTO>> listAll(@RequestBody ListAllBenefitCodeVO request) {
        if (request.getPageNum() == null) {
            request.setPageNum(1);
        }
        if (request.getPageSize() == null) {
            request.setPageSize(10);
        }

        return iBenefitCodeService.listAll(request);
    }

    @PostMapping("/listByUser")
    public CommonResult<List<ListAllBenefitCodeDTO>> listByUserId(@RequestBody ListUserBenefitCodeVO request) {
        if (request.getPageNum() == null) {
            request.setPageNum(1);
        }
        if (request.getPageSize() == null) {
            request.setPageSize(10);
        }

        return iBenefitCodeService.listByUserId(request);
    }

    @PostMapping("/queryByUserName")
    public CommonResult<List<ListAllBenefitCodeDTO>> queryByUserName(@RequestBody queryByUserNameVO request) {
        if (request.getPageNum() == null) {
            request.setPageNum(1);
        }
        if (request.getPageSize() == null) {
            request.setPageSize(10);
        }

        return iBenefitCodeService.queryByUserName(request);
    }

    /**
     * 核销兑换码
     */
    @PostMapping("/check")
    public CommonResult<?> check(@RequestBody BenefitCodeCheckVO benefitCodeCheckVO, HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromHeader(request.getHeader("Authorization"));
        benefitCodeCheckVO.setRedeemedBy(jwtUtil.getUserIdFromToken(token));
        return iBenefitCodeService.check(benefitCodeCheckVO);
    }
}
