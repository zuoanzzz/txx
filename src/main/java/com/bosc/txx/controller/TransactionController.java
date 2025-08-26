package com.bosc.txx.controller;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.Transaction;
import com.bosc.txx.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 交易流水表 前端控制器
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping("/listAll")
    public CommonResult<List<Transaction>> listAll() {
        List<Transaction> list = transactionService.list();
        return CommonResult.success(list);
    }

    @GetMapping("/listAll/{accountId}")
    public CommonResult<List<Transaction>> listByAccountId(@PathVariable Long accountId) {
        List<Transaction> list = transactionService.listByAccountId(accountId);
        return CommonResult.success(list);
    }

}
