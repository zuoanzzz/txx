package com.bosc.txx.service.impl;

import com.bosc.txx.model.Transaction;
import com.bosc.txx.dao.TransactionMapper;
import com.bosc.txx.service.ITransactionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
