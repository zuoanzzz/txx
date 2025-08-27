package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.dao.AccountMapper;
import com.bosc.txx.dao.BenefitMapper;
import com.bosc.txx.dao.UserMapper;
import com.bosc.txx.model.Benefit;
import com.bosc.txx.model.BenefitCode;
import com.bosc.txx.dao.BenefitCodeMapper;
import com.bosc.txx.model.User;
import com.bosc.txx.service.IAccountService;
import com.bosc.txx.service.IBenefitCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosc.txx.vo.BenefitCodeCheckVO;
import com.bosc.txx.vo.BenefitCodeExchangeVO;
import com.bosc.txx.vo.TransferVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 兑换码表 服务实现类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Service
public class BenefitCodeServiceImpl extends ServiceImpl<BenefitCodeMapper, BenefitCode> implements IBenefitCodeService {

    @Autowired
    BenefitCodeMapper benefitCodeMapper;

    @Autowired
    BenefitMapper benefitMapper;

    @Autowired
    IAccountService accountService;

    @Autowired
    UserMapper userMapper;

    @Override
    public CommonResult<?> getBenefitCodes(BenefitCodeExchangeVO request) {
        if (request == null
                || request.getAccountId() == null
                || request.getBenefitId() == null
                || request.getCount() == null) {
            return CommonResult.failed();
        }

        long count;
        try {
            count = Long.parseLong(request.getCount().trim());
            if (count <= 0) return CommonResult.failed();
        } catch (NumberFormatException nfe) {
            return CommonResult.failed();
        }

        // 1. 锁定对应 Benefit
        Benefit benefit = benefitMapper.selectByIdForUpdate(request.getBenefitId());
        if (benefit == null || Boolean.FALSE.equals(benefit.getActive())) {
            return CommonResult.failed();
        }

        // 2. 校验剩余数量
        if (benefit.getRemain() < count) {
            return CommonResult.failed();
        }

        User user = userMapper.selectUserByAccountId(request.getAccountId());

        // 3. 调用 transfer 完成交易
        TransferVO transferVO = new TransferVO();
        transferVO.setSourceAccountId(request.getAccountId());
        transferVO.setSourceAccountType("PERSONAL");
        transferVO.setSourceName(user.getName()); // 可从账户表查
        transferVO.setTargetAccountId(benefit.getAccountId());
        transferVO.setTargetAccountType("BENEFIT");
        transferVO.setTargetName(benefit.getName());
        transferVO.setAmount(String.valueOf(benefit.getPrice() * count));
        transferVO.setReason("兑换权益：" + benefit.getName());

        CommonResult<?> transferResult = accountService.transfer(transferVO);
        if (transferResult.getCode() != 200) {
            return CommonResult.failed();
        }

        // 4. 扣减剩余数量
        benefit.setRemain(benefit.getRemain() - (int) count);
        benefit.setUpdatedTime(LocalDateTime.now());
        benefitMapper.updateById(benefit);

        // 5. 生成兑换码
        for (int i = 0; i < count; i++) {
            String codeStr = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            BenefitCode code = new BenefitCode();
            code.setBenefitId(benefit.getId());
            code.setCode(codeStr);
            code.setUserId(Long.parseLong(user.getName())); // 兑换人
            code.setStatus("VALID");
            code.setExpDate(LocalDateTime.now().plusMonths(6));
            benefitCodeMapper.insert(code);
        }

        // 6. 返回结果
        return CommonResult.success();
    }

    @Override
    public CommonResult<List<Object>> listAll() {
        // 查询所有兑换码
        List<BenefitCode> codes = benefitCodeMapper.selectList(new QueryWrapper<BenefitCode>()
                .orderByDesc("created_time"));

        // 查询对应的权益名称
        List<Long> benefitIds = codes.stream()
                .map(BenefitCode::getBenefitId)
                .distinct()
                .collect(Collectors.toList());

        List<Benefit> benefits = benefitMapper.selectBatchIds(benefitIds);

        // 组装返回数据
        List<Object> result = codes.stream().map(code -> {
            Benefit benefit = benefits.stream()
                    .filter(b -> b.getId().equals(code.getBenefitId()))
                    .findFirst().orElse(null);

            return new Object() {
                public String accountId = code.getUserId() != null ? String.valueOf(code.getUserId()) : null;
                public String benefitId = code.getBenefitId() != null ? String.valueOf(code.getBenefitId()) : null;
                public String benefitName = benefit != null ? benefit.getName() : null;
                public Boolean status = "REDEEMED".equals(code.getStatus());
                public String codeStr = code.getCode();
            };
        }).collect(Collectors.toList());

        return CommonResult.success(result);
    }

    @Override
    public CommonResult<?> check(BenefitCodeCheckVO request) {
        if (request == null || request.getCodes() == null || request.getCodes().isEmpty()
                || request.getRedeemedBy() == null) {
            return CommonResult.failed();
        }

        // 查询待核销的兑换码
        List<BenefitCode> codes = benefitCodeMapper.selectList(new QueryWrapper<BenefitCode>()
                .in("code", request.getCodes()));

        if (codes.isEmpty()) {
            return CommonResult.failed();
        }

        LocalDateTime now = LocalDateTime.now();

        for (BenefitCode code : codes) {
            // 已核销或过期
            if ("REDEEMED".equals(code.getStatus()) ||
                    (code.getExpDate() != null && code.getExpDate().isBefore(now))) {
                continue; // 跳过
            }

            // 标记为已核销
            code.setStatus("REDEEMED");
            code.setRedeemedBy(Long.parseLong(request.getRedeemedBy()));
            code.setRedeemedAt(now);

            // 更新数据库
            benefitCodeMapper.updateById(code);
        }

        return CommonResult.success("核销完成");
    }
}
