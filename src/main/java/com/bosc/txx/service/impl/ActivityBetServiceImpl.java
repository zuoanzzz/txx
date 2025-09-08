package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosc.txx.dao.AccountMapper;
import com.bosc.txx.dao.ActivityMapper;
import com.bosc.txx.dao.UserMapper;
import com.bosc.txx.exception.BatchTransferException;
import com.bosc.txx.model.Account;
import com.bosc.txx.model.Activity;
import com.bosc.txx.model.ActivityBet;
import com.bosc.txx.dao.ActivityBetMapper;
import com.bosc.txx.model.User;
import com.bosc.txx.model.dto.ActivityBet.ActivityBetReq;
import com.bosc.txx.model.dto.account.TransferDTO;
import com.bosc.txx.service.IAccountService;
import com.bosc.txx.service.IActivityBetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 * 活动投注记录表 服务实现类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Service
public class ActivityBetServiceImpl extends ServiceImpl<ActivityBetMapper, ActivityBet> implements IActivityBetService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    IAccountService accountService;

    @Autowired
    private ActivityBetMapper activityBetMapper;
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bet(ActivityBetReq activityBetReq, Long userId) {
        User sourceUser = userMapper.selectById(userId);
        Account sourceAccount = accountMapper.selectOne(new QueryWrapper<Account>()
                .eq("user_id", sourceUser.getId()));
        Activity activity = activityMapper.selectById(activityBetReq.getActivityId());
        Account targetAccount = accountMapper.selectById(activity.getAccountId());

        // 执行转账操作
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setSourceName(sourceUser.getName());
        transferDTO.setTargetName(activity.getName());
        transferDTO.setSourceAccountId(sourceAccount.getAccountId());
        transferDTO.setTargetAccountId(targetAccount.getAccountId());
        transferDTO.setSourceAccountType(sourceAccount.getAccountType());
        transferDTO.setTargetAccountType(targetAccount.getAccountType());
        transferDTO.setAmount(activityBetReq.getAmount());
        transferDTO.setUsedFreeAmount(activityBetReq.getUsedFreeAmount());
        transferDTO.setReason("投注");
        Long txId = accountService.transfer(transferDTO);
        if (Objects.isNull(txId)) {
            throw new BatchTransferException("执行转账操作时发生错误");
        }

        ActivityBet activityBet = new ActivityBet();
        BeanUtils.copyProperties(activityBetReq, activityBet);
        activityBet.setAccountId(Long.valueOf(sourceAccount.getAccountId()));
        activityBet.setRelatedTxId(txId);
        Boolean ok = activityBetMapper.insert(activityBet) > 0;

        return ok;
    }
}
