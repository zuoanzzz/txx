package com.bosc.txx.service;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.controller.vo.transaction.BatchAccountImportExcelVO;
import com.bosc.txx.model.dto.account.TransferDTO;
import com.bosc.txx.model.dto.account.UserInfoDTO;
import com.bosc.txx.vo.account.AccountCreateVO;
import com.bosc.txx.vo.account.ListAllAccountVO;
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

    Integer createPersonalAccount(AccountCreateVO request);

    CommonResult<?> deleteAccount(Long accountId);

    CommonResult<List<Account>> listAllAccounts(ListAllAccountVO request);

    UserInfoDTO getByUserId(Long userId);

    Long transfer(TransferDTO transRequest);

    CommonResult<?> importAccounts(MultipartFile file);

    void importDataAsync(List<BatchAccountImportExcelVO> list, Long userIdFromToken);
}
