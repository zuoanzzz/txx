package com.bosc.txx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosc.txx.model.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统账户表 Mapper 接口
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

    Account selectByAccountId(@Param("accountId") String accountId);

    Account selectByAccountIdForUpdate(@Param("accountId") String accountId);
}
