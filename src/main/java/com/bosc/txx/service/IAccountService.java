package com.bosc.txx.service;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.vo.account.AccountCreateVO;
import com.bosc.txx.vo.account.ListAllAccountVO;
import com.bosc.txx.vo.account.TransferVO;
import com.bosc.txx.model.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 系统账户表 服务类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
public interface IAccountService extends IService<Account> {

    CommonResult<?> createPersonalAccount(AccountCreateVO request);

    CommonResult<?> deleteAccount(Long id);

    CommonResult<?> updateAccount(Account account);

    CommonResult<List<Account>> listAllAccounts(ListAllAccountVO request);

    CommonResult<?> transfer(TransferVO transRequest);

    CommonResult<?> importAccounts(MultipartFile file);
}
