package com.bosc.txx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosc.txx.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 现实员工表 Mapper 接口
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User selectUserByAccountId(@Param("accountId") String accountId);

    User selectUserByEmployeeNo(@Param("employeeNo") String employeeNo);
}
