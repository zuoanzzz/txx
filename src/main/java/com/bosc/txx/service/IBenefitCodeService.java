package com.bosc.txx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.BenefitCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosc.txx.model.dto.benefitcode.ListAllBenefitCodeDTO;
import com.bosc.txx.vo.benefitcode.BenefitCodeCheckVO;
import com.bosc.txx.vo.benefitcode.BenefitCodeExchangeVO;
import com.bosc.txx.vo.benefitcode.ListAllBenefitCodeVO;
import com.bosc.txx.vo.benefitcode.ListUserBenefitCodeVO;

import java.util.List;

/**
 * <p>
 * 兑换码表 服务类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
public interface IBenefitCodeService extends IService<BenefitCode> {

    CommonResult<?> getBenefitCodes(BenefitCodeExchangeVO request);

    CommonResult<List<ListAllBenefitCodeDTO>> listAll(ListAllBenefitCodeVO request);

    CommonResult<?> check(BenefitCodeCheckVO request);

    CommonResult<List<ListAllBenefitCodeDTO>> listByUserId(ListUserBenefitCodeVO request);
}
