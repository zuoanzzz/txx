package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosc.txx.controller.vo.transaction.BatchTransferImportExcelVO;
import com.bosc.txx.model.Transaction;
import com.bosc.txx.dao.TransactionMapper;
import com.bosc.txx.model.dto.account.TransferDTO;
import com.bosc.txx.service.IAccountService;
import com.bosc.txx.service.ITransactionService;
import com.bosc.txx.dao.UserMapper;
import com.bosc.txx.dao.AccountMapper;
import com.bosc.txx.model.User;
import com.bosc.txx.model.Account;
import com.bosc.txx.exception.BatchTransferException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 交易流水表 服务实现类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Service
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements ITransactionService {
    
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private IAccountService accountService;
    
    @Override
    public List<Transaction> listByAccountId(Long accountId) {
        if (accountId == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getTargetAccountId, accountId)
                .or(w -> w.eq(Transaction::getSourceAccountId, accountId));
        return this.list(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)   // 异常回滚所有导入
    @Async
    @Override
    public void importDataAsync(List<BatchTransferImportExcelVO> list, Long createdBy) {
        if (list == null || list.isEmpty()) {
            return;
        }
        
        // 获取系统账户ID（用于批量转账的源账户）
        Account sourceAccount = accountMapper.selectById(createdBy);
        User sourceUser = userMapper.selectById(createdBy);
        if (sourceAccount == null) {
            throw new BatchTransferException("系统账户不存在，无法执行批量转账");
        }
        
        for (BatchTransferImportExcelVO transferItem : list) {
            // 1. 根据工号查找用户
            User targetUser = userMapper.selectUserByEmployeeNo(transferItem.getEmployeeNo());
            if (targetUser == null) {
                throw new BatchTransferException(transferItem.getEmployeeNo(), "用户不存在", 
                    "根据工号未找到对应的用户");
            }
            
            // 2. 根据用户ID查找个人账户
            Account targetAccount = accountMapper.selectOne(new QueryWrapper<Account>()
                    .eq("user_id", sourceUser.getId()));
            if (targetAccount == null) {
                throw new BatchTransferException(transferItem.getEmployeeNo(), "账户不存在", 
                    "用户没有对应的个人账户");
            }

            // 执行转账操作
            TransferDTO transferDTO = new TransferDTO();
            transferDTO.setSourceName(sourceUser.getName());
            transferDTO.setTargetName(targetUser.getName());
            transferDTO.setSourceAccountId(sourceAccount.getAccountId());
            transferDTO.setTargetAccountId(targetAccount.getAccountId());
            transferDTO.setSourceAccountType(sourceAccount.getAccountType());
            transferDTO.setTargetAccountType(targetAccount.getAccountType());
            transferDTO.setAmount(Long.valueOf(transferItem.getAmount()));
            transferDTO.setReason(transferItem.getRemark());
            Long txId = accountService.transfer(transferDTO);

            if (Objects.isNull(txId)) {
                throw new BatchTransferException(transferItem.getEmployeeNo(), "转账失败", 
                    "执行转账操作时发生错误");
            }
        }
        
        log.info("批量转账完成，共处理 {} 条记录", list.size());
    }
}
