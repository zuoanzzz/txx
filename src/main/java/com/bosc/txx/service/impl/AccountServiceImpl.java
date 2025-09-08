package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bosc.txx.common.CommonResult;
import com.bosc.txx.controller.vo.transaction.BatchAccountImportExcelVO;
import com.bosc.txx.dao.TransactionMapper;
import com.bosc.txx.dao.UserMapper;
import com.bosc.txx.model.dto.account.TransferDTO;
import com.bosc.txx.model.dto.account.UserInfoDTO;
import com.bosc.txx.vo.account.AccountCreateVO;
import com.bosc.txx.vo.account.ListAllAccountVO;
import com.bosc.txx.model.Account;
import com.bosc.txx.dao.AccountMapper;
import com.bosc.txx.model.Transaction;
import com.bosc.txx.model.User;
import com.bosc.txx.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosc.txx.util.AccountIdGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * <p>
 * 系统账户表 服务实现类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    private static final String DEFAULT_PASSWORD = "B@s95594!";

    // 用于创建个人账户
    @Override
    public Integer createPersonalAccount(AccountCreateVO request) {

        System.out.println("开始创建user");
        // 1.查user，若无则插入记录
        User user = userMapper.selectUserByEmployeeNo(request.getEmployeeNo());
        if (user == null) {
            User this_user = new User();
            this_user.setEmployeeNo(request.getEmployeeNo());
            this_user.setName(request.getName());
            this_user.setDepartment(request.getDepartment());
            this_user.setCreatedTime(LocalDateTime.now());
            this_user.setUpdatedTime(LocalDateTime.now());

            //设置初始密码
            this_user.setPassword(encryptPassword());

            this_user.setRole("NORMAL");
            user = this_user;
            userMapper.insert(this_user);
        }

        // 2.创建account
        Account account = new Account();
        account.setUserId(user.getId());
        account.setAccountId(AccountIdGenerator.generateAccountId()); // 可自定义生成规则
        account.setAccountType("PERSONAL"); // 默认个人账户
        account.setBalance(0L); // 初始余额
        account.setDeleted(false);
        account.setCreatedBy(Long.valueOf(request.getCreatedBy()));
        account.setCreatedTime(LocalDateTime.now());
        account.setUpdatedTime(LocalDateTime.now());
        Integer result = accountMapper.insert(account);

        return result;
    }

    private String encryptPassword() {
        String newPwdEnc = md5Hex(DEFAULT_PASSWORD);
        return newPwdEnc;
    }

    @Override
    public CommonResult<?> deleteAccount(String accountId) {
        // 查询账户
        Account account = accountMapper.selectByAccountId(accountId);
        if (account == null) {
            return CommonResult.failed(); // 账户不存在，使用统一失败返回
        }

        // 逻辑删除
        account.setDeleted(true);
        account.setUpdatedTime(LocalDateTime.now());
        int rows = accountMapper.updateById(account);
        if (rows > 0) {
            return CommonResult.success("账户删除成功"); // 成功，返回 message
        } else {
            return CommonResult.failed(); // 删除失败，使用统一失败返回
        }
    }


    @Override
    public CommonResult<List<Account>> listAllAccounts(ListAllAccountVO request) {
        // 查询 deleted = false 的账户
        Page<Account> page = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<Account> query = new QueryWrapper<>();
        query.eq("deleted", false);
        IPage<Account> accounts = accountMapper.selectPage(page, query);

        return CommonResult.success(accounts.getRecords());
    }

    @Override
    public UserInfoDTO getByUserId(Long userId) {
        User user = userMapper.selectById(userId);
        Account account = accountMapper.selectByUserId(userId);

        if (user == null || account == null) {
            return null;
        }

        UserInfoDTO result = new UserInfoDTO();
        result.setEmployeeNo(user.getEmployeeNo());
        result.setName(user.getName());
        result.setDepartment(user.getDepartment());
        result.setEmail(user.getEmail());
        result.setPhone(user.getPhone());
        result.setRole(user.getRole());
        result.setCreatedTime(user.getCreatedTime());
        result.setUpdatedTime(user.getUpdatedTime());
        result.setLastLogin(user.getLastLogin());

        result.setAccountId(account.getAccountId());
        result.setAccountType(account.getAccountType());
        result.setBalance(account.getBalance());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long transfer(TransferDTO transRequest) {
        LocalDateTime start_time = LocalDateTime.now();

        // 1. 基本参数校验
        if (transRequest == null) {
            return null;
        }
        if (transRequest.getSourceAccountId() == null || transRequest.getTargetAccountId() == null) {
            return null;
        }
        if (transRequest.getAmount() == null) {
            return null;
        }


        // 2. 解析金额（整数）
        final long amount;
        try {
            amount = transRequest.getAmount();
            if (amount <= 0L) {
                return null;
            }
        } catch (NumberFormatException nfe) {
            return null;
        }

        // 3. 禁止同账户互转
        if (transRequest.getSourceAccountId().equals(transRequest.getTargetAccountId())) {
            return null;
        }

        // 4. 固定加锁顺序（按 accountId 字典序）以避免死锁
        String aId = transRequest.getSourceAccountId();
        String bId = transRequest.getTargetAccountId();
        String firstId = (aId.compareTo(bId) <= 0) ? aId : bId;
        String secondId = (aId.compareTo(bId) <= 0) ? bId : aId;

        Account firstLocked = accountMapper.selectByAccountIdForUpdate(firstId);
        Account secondLocked = accountMapper.selectByAccountIdForUpdate(secondId);
        if (firstLocked == null || secondLocked == null) {
            return null;
        }

        // 5. 映射回 src/tgt
        Account src = firstLocked.getAccountId().equals(aId) ? firstLocked : secondLocked;
        Account tgt = firstLocked.getAccountId().equals(aId) ? secondLocked : firstLocked;

        // 6. 存在性与删除标志校验
        if (src == null || tgt == null) {
            return null;
        }
        if (Boolean.TRUE.equals(src.getDeleted()) || Boolean.TRUE.equals(tgt.getDeleted())) {
            return null;
        }

        // 7. 直接使用请求中的 accountType，并决定 txType
        String srcType = transRequest.getSourceAccountType().trim().toUpperCase();
        String tgtType = transRequest.getTargetAccountType().trim().toUpperCase();

        String txType = determineTxType(srcType, tgtType);
        if (txType == null) {
            return null;
        }

        // 8. 余额校验（GRANT 不校验）
        if (!"GRANT".equals(txType)) {
            long srcBal = src.getBalance();
            if ("ACTIVITY_BET".equals(txType)) {
                srcBal += transRequest.getUsedFreeAmount();
            }
            if (srcBal < amount) {
                return null;
            }
        }

        // 9. 更新余额并持久化（行锁已生效）
        try {
            if (!"GRANT".equals(txType)) {
                long currentSrc = src.getBalance();
                long newSrc;
                if ("ACTIVITY_BET".equals(txType)) {
                    long change = transRequest.getAmount() - transRequest.getUsedFreeAmount();
                    newSrc = Math.subtractExact(currentSrc, change);
                } else {
                    newSrc = Math.subtractExact(currentSrc, amount);
                }
                src.setBalance(newSrc);
                src.setUpdatedTime(LocalDateTime.now());
                accountMapper.updateById(src);
            }

            long currentTgt = tgt.getBalance();
            long newTgt = Math.addExact(currentTgt, amount);
            tgt.setBalance(newTgt);
            tgt.setUpdatedTime(LocalDateTime.now());
            accountMapper.updateById(tgt);
        } catch (ArithmeticException ae) {
            // 溢出/下溢，抛出异常使事务回滚
            throw ae;
        }

        // 10. 插入流水 Transaction
        Transaction tx = new Transaction();
        tx.setTxNo(UUID.randomUUID().toString().replace("-", ""));
        tx.setSourceAccountId(Long.valueOf(src.getAccountId()));
        tx.setTargetAccountId(Long.valueOf(tgt.getAccountId()));
        tx.setSourceName(transRequest.getSourceName());
        tx.setTargetName(transRequest.getTargetName());
        tx.setSourceAccountType(srcType);
        tx.setTargetAccountType(tgtType);
        tx.setAmount(amount);
        tx.setTxType(txType);
        tx.setReason(transRequest.getReason());
        tx.setCreatedBy(transRequest.getCreatedBy());
        tx.setRelatedBetId(null);         // 后面由调用者写入这个字段
        tx.setMetadata(null);
        tx.setStartTime(start_time);
        tx.setEndTime(LocalDateTime.now());
        transactionMapper.insert(tx);

        // 11. 返回流水对象
        return tx.getId();
    }

    /**
     * 根据请求中的源/目标账户类型直接判断交易类型
     * 1. TRANSFER: personal/activity -> personal
     * 2. GRANT: super -> personal
     * 3. ACTIVITY_BET: personal -> activity
     * 4. BENEFIT_REDEEM: personal -> benefit
     * 返回 tx_type 字符串或 null（表示不允许）
     */
    private String determineTxType(String srcTypeUpper, String tgtTypeUpper) {
        if ((srcTypeUpper.equals("PERSONAL") || srcTypeUpper.equals("ACTIVITY")) && tgtTypeUpper.equals("PERSONAL")) {
            return "TRANSFER";
        }
        if (srcTypeUpper.equals("SUPER") && tgtTypeUpper.equals("PERSONAL")) {
            return "GRANT";
        }
        if (srcTypeUpper.equals("PERSONAL") && tgtTypeUpper.equals("ACTIVITY")) {
            return "ACTIVITY_BET";
        }
        if (srcTypeUpper.equals("PERSONAL") && tgtTypeUpper.equals("BENEFIT")) {
            return "BENEFIT_REDEEM";
        }
        return null;
    }


    @Transactional
    public CommonResult<?> importAccounts(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            List<User> users = new ArrayList<>();
            List<Account> accounts = new ArrayList<>();
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // 跳过空行

                if (firstLine) {
                    firstLine = false; // 跳过表头
                    continue;
                }

                // 简单分割（注意：不支持带引号的复杂 CSV）
                String[] cols = line.split(",", -1); // -1 保留空字段
                if (cols.length < 3) continue;

                String name = cols[0].trim();
                String employeeNo = cols[1].trim();
                String department = cols[2].trim();

                // 创建用户
                User user = new User();
                user.setName(name);
                user.setEmployeeNo(employeeNo);
                user.setDepartment(department);
                user.setPassword(encryptPassword());
                user.setRole("NORMAL");
                users.add(user);
            }

            // 批量插入用户
            if (!users.isEmpty()) {
                for (User user : users) {
                    userMapper.insert(user);

                    // 创建对应账户
                    Account account = new Account();
                    account.setUserId(user.getId());
                    account.setAccountId(AccountIdGenerator.generateAccountId()); // 可自定义生成规则
                    account.setAccountType("PERSONAL"); // 默认个人账户
                    account.setBalance(0L); // 初始余额
                    account.setDeleted(false);
                    account.setCreatedBy(Long.valueOf("1"));
                    account.setCreatedTime(LocalDateTime.now());
                    account.setUpdatedTime(LocalDateTime.now());
                    accounts.add(account);
                }

                // 批量插入账户
                for (Account account : accounts) {
                    accountMapper.insert(account);
                }
            }

            return CommonResult.success();

        } catch (IOException e) {
            e.printStackTrace();
            return CommonResult.failed();
        }
    }

    @Override
    @Transactional
    public void importDataAsync(List<BatchAccountImportExcelVO> list, Long userIdFromToken) {
        // 批量插入用户
        if (!list.isEmpty()) {
            List<Account> accounts = new ArrayList<>();
            for (BatchAccountImportExcelVO batchAccountImportExcelVO : list) {
                // 创建用户
                User user = new User();
                user.setName(batchAccountImportExcelVO.getName());
                user.setEmployeeNo(batchAccountImportExcelVO.getEmployeeNo());
                user.setDepartment(batchAccountImportExcelVO.getDepartment());
                user.setPassword(md5Hex(DEFAULT_PASSWORD));
                user.setRole("NORMAL");
                userMapper.insert(user);

                // 创建对应账户
                Account account = new Account();
                account.setUserId(user.getId());
                account.setAccountId(AccountIdGenerator.generateAccountId());
                account.setAccountType("PERSONAL");
                account.setBalance(0L);
                account.setDeleted(false);
                account.setCreatedBy(userIdFromToken);
                accounts.add(account);
            }

            // 批量插入账户
            for (Account account : accounts) {
                accountMapper.insert(account);
            }
        }
    }
}
