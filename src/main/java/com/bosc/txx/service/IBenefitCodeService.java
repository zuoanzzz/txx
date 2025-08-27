package com.bosc.txx.service;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.BenefitCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosc.txx.vo.BenefitCodeCheckVO;
import com.bosc.txx.vo.BenefitCodeExchangeVO;

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

    CommonResult<List<Object>> listAll();

    CommonResult<?> check(BenefitCodeCheckVO request);
}
