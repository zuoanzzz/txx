package com.bosc.txx.controller;


import com.bosc.txx.vo.account.AccountCreateVO;
import com.bosc.txx.vo.account.ListAllAccountVO;
import com.bosc.txx.vo.account.TransferVO;
import com.bosc.txx.model.Account;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 新增账户
     */
    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody AccountCreateVO request) {
        return iaccountService.createPersonalAccount(request);
    }

    /**
     * 删除账户
     */
    @DeleteMapping("/delete/{id}")
    public CommonResult<?> delete(@PathVariable Long id) {
        return iaccountService.deleteAccount(id);
    }

    /**
     * 编辑账户（不准确，应该是账户加用户的信息都在一个接口里修改）
     */
    @PutMapping("/update")
    public CommonResult<?> update(@RequestBody Account account) {
        return iaccountService.updateAccount(account);
    }

    /**
     * 查询所有账户
     */
    @GetMapping("/listAll")
    public CommonResult<List<Account>> listAll(@RequestBody ListAllAccountVO request) {
        return iaccountService.listAllAccounts(request);
    }

    /**
     * 交易接口（转账、流水）
     */
    @PostMapping("/trans")
    public CommonResult<Long> transfer(@RequestBody TransferVO transRequest) {
        Long txId = iaccountService.transfer(transRequest);
        return CommonResult.success(txId);
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
}
