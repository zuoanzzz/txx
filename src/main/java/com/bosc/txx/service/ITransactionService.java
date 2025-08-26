package com.bosc.txx.service;

import com.bosc.txx.model.Transaction;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 交易流水表 服务类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
public interface ITransactionService extends IService<Transaction> {
    /**
     * 根据用户ID查询其相关的交易流水
     * 规则：用户名下任一账户作为源或目标参与的流水，或由该用户创建的流水
     */
    List<Transaction> listByAccountId(Long accountId);
}
