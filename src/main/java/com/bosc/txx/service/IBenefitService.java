package com.bosc.txx.service;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.Benefit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosc.txx.vo.benefit.BenefitCreateVO;
import com.bosc.txx.vo.benefit.GetAllBenefitVO;
import com.bosc.txx.vo.benefit.ListAllBenefitVO;

import java.util.List;

/**
 * <p>
 * 权益商品表 服务类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
public interface IBenefitService extends IService<Benefit> {

    CommonResult<List<Benefit>> listAllBenefits(ListAllBenefitVO request);

    CommonResult<?> createBenefit(BenefitCreateVO vo);

    CommonResult<?> updateBenefit(BenefitCreateVO vo);

    CommonResult<?> deleteBenefit(Long id);

    CommonResult<List<Benefit>> getAllBenefit(GetAllBenefitVO request);
}
