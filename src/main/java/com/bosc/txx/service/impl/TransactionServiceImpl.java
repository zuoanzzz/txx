package com.bosc.txx.service.impl;

import com.bosc.txx.model.Transaction;
import com.bosc.txx.dao.TransactionMapper;
import com.bosc.txx.service.ITransactionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
}
