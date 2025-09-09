package com.bosc.txx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosc.txx.model.Account;
import com.bosc.txx.model.User;
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

    Account selectByAccountId(@Param("accountId") Long accountId);

    Account selectByAccountIdForUpdate(@Param("accountId") Long accountId);

    Account selectByUserId(@Param("userId") Long userId);
}
