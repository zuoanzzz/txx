package com.bosc.txx.controller;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.controller.vo.transaction.BatchTransferImportExcelVO;
import com.bosc.txx.model.Transaction;
import com.bosc.txx.service.ITransactionService;
import com.bosc.txx.util.ExcelUtils;
import com.bosc.txx.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
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

    @Autowired
    private JwtUtil jwtUtil;

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

    @GetMapping("/get-import-template")
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<BatchTransferImportExcelVO> list = Arrays.asList(
                BatchTransferImportExcelVO.builder().build());
        // 输出
        ExcelUtils.write(response, "批量转账导入模板.xls", "批量转账", BatchTransferImportExcelVO.class, list);
    }

    /**
     * 批量发放-excel
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/import-excel")
    public CommonResult<Boolean> importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        List<BatchTransferImportExcelVO> list = ExcelUtils.read(file, BatchTransferImportExcelVO.class);

        String token = jwtUtil.extractTokenFromHeader(request.getHeader("Authorization"));
        transactionService.importDataAsync(list, jwtUtil.getUserIdFromToken(token));
        return CommonResult.success(true);
    }

    /**
     * 批量发放-list
     * @param list
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/import-list")
    public CommonResult<Boolean> importList(@RequestBody List<BatchTransferImportExcelVO> list, HttpServletRequest request) throws IOException {
        String token = jwtUtil.extractTokenFromHeader(request.getHeader("Authorization"));
        transactionService.importDataAsync(list, jwtUtil.getUserIdFromToken(token));
        return CommonResult.success(true);
    }

}
