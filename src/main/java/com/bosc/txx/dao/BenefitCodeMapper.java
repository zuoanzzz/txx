package com.bosc.txx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bosc.txx.model.BenefitCode;
import com.bosc.txx.model.dto.benefitcode.ListAllBenefitCodeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 兑换码表 Mapper 接口
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
public interface BenefitCodeMapper extends BaseMapper<BenefitCode> {

    IPage<ListAllBenefitCodeDTO> listAllBenefitCodes(Page<?> page);

    IPage<ListAllBenefitCodeDTO> listByUserIdPage(Page<ListAllBenefitCodeDTO> page, @Param("userId") Long userId);
}
