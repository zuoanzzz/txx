package com.bosc.txx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosc.txx.model.Benefit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 权益商品表 Mapper 接口
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Mapper
public interface BenefitMapper extends BaseMapper<Benefit> {

    Benefit selectByIdForUpdate(@Param("benefitId")String benefitId);
}
