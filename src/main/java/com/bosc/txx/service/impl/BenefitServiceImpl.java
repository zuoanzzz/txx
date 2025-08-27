package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.dao.AccountMapper;
import com.bosc.txx.model.Account;
import com.bosc.txx.model.Benefit;
import com.bosc.txx.dao.BenefitMapper;
import com.bosc.txx.service.IBenefitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosc.txx.util.AccountIdGenerator;
import com.bosc.txx.vo.BenefitCreateVO;
import com.bosc.txx.vo.BenefitResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 权益商品表 服务实现类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Service
public class BenefitServiceImpl extends ServiceImpl<BenefitMapper, Benefit> implements IBenefitService {

    @Autowired
    private BenefitMapper benefitMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public CommonResult<List<Benefit>> listAllBenefits() {
        QueryWrapper<Benefit> q = new QueryWrapper<>();
        q.eq("deleted", 0)
                .and(wrapper -> wrapper.isNull("exp_date")
                        .or().ge("exp_date", LocalDateTime.now()));
        List<Benefit> list = benefitMapper.selectList(q);
        return CommonResult.success(list);
    }

    @Override
    public CommonResult<?> createBenefit(BenefitCreateVO vo) {
        // 1. 参数校验
        if (vo.getName() == null || vo.getName().trim().isEmpty()) {
            return CommonResult.failed();
        }
        if (vo.getPrice() == null || vo.getPrice() <= 0) {
            return CommonResult.failed();
        }
        if (vo.getTotal() == null || vo.getTotal() <= 0) {
            return CommonResult.failed();
        }
        if (vo.getExpDate() != null && LocalDateTime.parse(vo.getExpDate()).isBefore(LocalDateTime.now())) {
            return CommonResult.failed();
        }

        // 2. VO -> 实体类
        Benefit benefit = new Benefit();
        benefit.setName(vo.getName());
        benefit.setDescription(vo.getDescription());
        benefit.setPrice(vo.getPrice());
        benefit.setImage(Long.valueOf(vo.getImage()));
        benefit.setTotal(vo.getTotal());
        benefit.setRemain(vo.getTotal());
        benefit.setExpDate(LocalDateTime.parse(vo.getExpDate()));
        benefit.setActive(Boolean.TRUE);

        // 3. 系统字段填充
        benefit.setDeleted(false);
        benefit.setCreatedBy(Long.valueOf(vo.getUserId()));
        benefit.setCreatedTime(LocalDateTime.now());
        benefit.setUpdatedTime(LocalDateTime.now());

        // 4.为权益创建账户
        Account account = new Account();
        account.setAccountId(AccountIdGenerator.generateAccountId()); // 可自定义生成规则
        account.setAccountType("BENEFIT"); // 默认个人账户
        account.setBalance(0L); // 初始余额
        account.setDeleted(false);
        account.setCreatedBy(Long.valueOf(vo.getUserId()));
        account.setCreatedTime(LocalDateTime.now());
        account.setUpdatedTime(LocalDateTime.now());
        accountMapper.insert(account);
        benefit.setAccountId(account.getAccountId());

        // 5. 插入数据库
        benefitMapper.insert(benefit);

        return CommonResult.success("新增权益成功");
    }

    @Override
    public CommonResult<?> updateBenefit(BenefitCreateVO vo) {
        // 1. 查询是否存在
        Benefit benefit = benefitMapper.selectById(vo.getId());
        if (benefit == null || Boolean.TRUE.equals(benefit.getDeleted())) {
            return CommonResult.failed();
        }

        // 2. 参数校验（和 create 类似）
        if (vo.getName() == null || vo.getName().trim().isEmpty()) {
            return CommonResult.failed();
        }
        if (vo.getPrice() == null || vo.getPrice() <= 0) {
            return CommonResult.failed();
        }
        if (vo.getTotal() == null || vo.getTotal() <= 0) {
            return CommonResult.failed();
        }
        if (vo.getRemain() == null || vo.getRemain() > vo.getTotal()) {
            return CommonResult.failed();
        }
        if (vo.getExpDate() != null && LocalDateTime.parse(vo.getExpDate()).isBefore(LocalDateTime.now())) {
            return CommonResult.failed();
        }

        // 3. 更新字段
        benefit.setName(vo.getName());
        benefit.setDescription(vo.getDescription());
        benefit.setPrice(vo.getPrice());
        benefit.setImage(Long.valueOf(vo.getImage()));
        benefit.setTotal(vo.getTotal());
        benefit.setRemain(vo.getRemain());
        benefit.setExpDate(LocalDateTime.parse(vo.getExpDate()));
        benefit.setActive(vo.getActive());

        benefit.setUpdatedTime(LocalDateTime.now());

        // 4. 更新数据库
        benefitMapper.updateById(benefit);

        return CommonResult.success("修改权益成功");
    }

    @Override
    public CommonResult<?> deleteBenefit(Long id) {
        // 1. 查询权益是否存在
        Benefit benefit = benefitMapper.selectById(id);
        if (benefit == null || Boolean.TRUE.equals(benefit.getDeleted())) {
            return CommonResult.failed();
        }

        // 2. 设置逻辑删除标志
        benefit.setDeleted(true);
        benefit.setUpdatedTime(LocalDateTime.now());

        // 3. 更新数据库
        benefitMapper.updateById(benefit);

        return CommonResult.success("删除权益成功");
    }
}
