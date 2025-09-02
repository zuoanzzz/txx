package com.bosc.txx.controller;


import com.bosc.txx.controller.vo.transaction.BatchAccountImportExcelVO;
import com.bosc.txx.controller.vo.transaction.BatchTransferImportExcelVO;
import com.bosc.txx.dao.AccountMapper;
import com.bosc.txx.dao.UserMapper;
import com.bosc.txx.model.User;
import com.bosc.txx.model.dto.account.TransferDTO;
import com.bosc.txx.model.dto.account.UserInfoDTO;
import com.bosc.txx.util.ExcelUtils;
import com.bosc.txx.vo.account.AccountCreateVO;
import com.bosc.txx.vo.account.ListAllAccountVO;
import com.bosc.txx.vo.account.TransferVO;
import com.bosc.txx.model.Account;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.service.IAccountService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 系统账户表 前端控制器
 * </p>
 *
 * @author
 * @since 2025-08-25
 */

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService iaccountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 新增账户
     */
    @PostMapping("/create")
    public CommonResult<Integer> create(@RequestBody AccountCreateVO request) {
        Integer result = iaccountService.createPersonalAccount(request);
        return CommonResult.success(result);
    }

    /**
     * 删除账户
     */
    @DeleteMapping("/delete/{accountId}")
    public CommonResult<?> delete(@PathVariable String accountId) {
        return iaccountService.deleteAccount(accountId);
    }

    /**
     * 查询所有账户
     */
    @PostMapping("/listAll")
    public CommonResult<List<Account>> listAll(@RequestBody ListAllAccountVO request) {
        return iaccountService.listAllAccounts(request);
    }

    /**
     * 根据userId查询账户信息
     */
    @PostMapping("/getByUserId/{userId}")
    public CommonResult<UserInfoDTO> getByUserId(@PathVariable Long userId){
        UserInfoDTO result = iaccountService.getByUserId(userId);
        if(result == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(result);
    }

    /**
     * 交易接口（转账、流水）
     */
    @PostMapping("/trans")
    public CommonResult<Long> transfer(@RequestBody TransferVO transRequest) {
        Account srcAccount = accountMapper.selectByUserId(transRequest.getCreatedBy());
        TransferDTO request = new TransferDTO();
        Account src = accountMapper.selectByAccountId(srcAccount.getAccountId());
        Account tgt = accountMapper.selectByAccountId(transRequest.getTargetAccountId());

        if("PERSONAL".equals(src.getAccountType())) {
            User srcUser = userMapper.selectById(src.getUserId());
            request.setSourceName(srcUser.getName());
        }

        if("PERSONAL".equals(tgt.getAccountType())) {
            User tgtUser = userMapper.selectById(tgt.getUserId());
            request.setTargetName(tgtUser.getName());
        }

        request.setSourceAccountId(srcAccount.getAccountId());
        request.setSourceAccountType(src.getAccountType());
        request.setTargetAccountId(transRequest.getTargetAccountId());
        request.setTargetAccountType(tgt.getAccountType());

        request.setAmount(transRequest.getAmount());
        request.setReason(transRequest.getReason());
        request.setCreatedBy(transRequest.getCreatedBy());

        Long txId = iaccountService.transfer(request);
        if(txId == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(txId);
    }

    @GetMapping("/get-import-template")
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<BatchAccountImportExcelVO> list = Arrays.asList(
                BatchAccountImportExcelVO.builder().build());
        // 输出
        ExcelUtils.write(response, "批量导入用户模板.xls", "导入用户", BatchAccountImportExcelVO.class, list);
    }

    /**
     * 批量导入
     */
    @PostMapping("/import")
    public CommonResult<?> importAccounts(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return CommonResult.failed();
        }
        return iaccountService.importAccounts(file);
    }

    @GetMapping("/getBalance/{userId}")
    public CommonResult<Long> getBalance(@PathVariable Long userId){
        Account account = accountMapper.selectByUserId(userId);
        if(account == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(account.getBalance());
    }
}
