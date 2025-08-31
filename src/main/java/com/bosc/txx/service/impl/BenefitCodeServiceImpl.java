package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.dao.BenefitMapper;
import com.bosc.txx.dao.UserMapper;
import com.bosc.txx.model.Benefit;
import com.bosc.txx.model.BenefitCode;
import com.bosc.txx.dao.BenefitCodeMapper;
import com.bosc.txx.model.User;
import com.bosc.txx.model.dto.account.TransferDTO;
import com.bosc.txx.model.dto.benefitcode.ListAllBenefitCodeDTO;
import com.bosc.txx.service.IAccountService;
import com.bosc.txx.service.IBenefitCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosc.txx.vo.benefitcode.BenefitCodeCheckVO;
import com.bosc.txx.vo.benefitcode.BenefitCodeExchangeVO;
import com.bosc.txx.vo.benefitcode.ListAllBenefitCodeVO;
import com.bosc.txx.vo.benefitcode.ListUserBenefitCodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
    @Transactional
    public CommonResult<?> getBenefitCodes(BenefitCodeExchangeVO request) {
        if (request == null
                || request.getAccountId() == null
                || request.getBenefitId() == null
                || request.getCount() == null) {
            return CommonResult.failed();
        }

        int count;
        try {
            count = Integer.parseInt(request.getCount().trim());
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
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setSourceAccountId(request.getAccountId());
        transferDTO.setSourceAccountType("PERSONAL");
        transferDTO.setSourceName(user.getName()); // 可从账户表查
        transferDTO.setTargetAccountId(benefit.getAccountId());
        transferDTO.setTargetAccountType("BENEFIT");
        transferDTO.setTargetName(benefit.getName());
        transferDTO.setAmount(benefit.getPrice() * count);
        transferDTO.setReason("兑换权益：" + benefit.getName());
        transferDTO.setCreatedBy(request.getUserId());

        Long txId = accountService.transfer(transferDTO);
        if (Objects.isNull(txId)) {
            return CommonResult.failed();
        }

        // 4. 扣减剩余数量
        benefit.setRemain(benefit.getRemain() - count);
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
    public CommonResult<List<ListAllBenefitCodeDTO>> listAll(ListAllBenefitCodeVO request) {
        Page<ListAllBenefitCodeDTO> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<ListAllBenefitCodeDTO> result = benefitCodeMapper.listAllBenefitCodes(page);
        return CommonResult.success(result.getRecords());
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

    @Override
    public CommonResult<List<ListAllBenefitCodeDTO>> listByUserId(ListUserBenefitCodeVO request) {
        Page<ListAllBenefitCodeDTO> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<ListAllBenefitCodeDTO> result = benefitCodeMapper.listByUserIdPage(page, request.getUserId());
        return CommonResult.success(result.getRecords());
    }
}
