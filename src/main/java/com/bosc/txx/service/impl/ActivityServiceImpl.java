package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosc.txx.dao.*;
import com.bosc.txx.model.Account;
import com.bosc.txx.model.Activity;
import com.bosc.txx.model.ActivityBet;
import com.bosc.txx.model.dto.activitybet.ActivityBetUserResult;
import com.bosc.txx.model.dto.activitybet.ActivityBetWorkResult;
import com.bosc.txx.service.IActivityService;
import com.bosc.txx.util.AccountIdGenerator;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private ActivityBetMapper activityBetMapper;
    @Autowired
    private WorkMapper workMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Activity getByName(String name) {
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("name", name));
        fillStatus(activity);
        return activity;
    }

    @Override
    public Boolean createActivityAccount(Activity activity, Long userId) {
        Account account = new Account();
        account.setCreatedBy(userId);
        account.setAccountType("ACTIVITY");
        account.setAccountId(AccountIdGenerator.generateAccountId());
        account.setUserId(Long.valueOf(AccountIdGenerator.generateAccountId()));
        accountMapper.insert(account);

        activity.setAccountId(account.getId());
        int row = activityMapper.insert(activity);

        return row > 0;
    }

    @Override
    public void createUserSheet(Long id, Sheet userSheet, CellStyle headerStyle) {
        // 创建表头
        Row headerRow = userSheet.createRow(0);
        String[] headers = {"活动名称", "作品名称", "投注人", "投注金额", "投注时间"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        List<ActivityBet> list = activityBetMapper.selectList(new QueryWrapper<ActivityBet>()
                .eq("activity_id", id)
                .groupBy("account_id")
                .groupBy("work_id"));
        Activity activity = activityMapper.selectById(list.get(0).getActivityId());

        List<ActivityBetUserResult> betList = new ArrayList<>();
        for (ActivityBet activityBet : list) {
            ActivityBetUserResult betResult = new ActivityBetUserResult();
            betResult.setActivityName(activity.getName());
            betResult.setWorkName(workMapper.selectById(activityBet.getWorkId()).getTitle());
            Account account = accountMapper.selectById(activityBet.getAccountId());
            betResult.setBetName(userMapper.selectById(account.getUserId()).getName());
            betResult.setBetAmount(activityBet.getAmount());
            betResult.setBetDate(activityBet.getCreatedTime());
            betList.add(betResult);
        }

        // 填充数据
        int rowNum = 1;
        for (ActivityBetUserResult result : betList) {
            Row row = userSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(result.getActivityName());
            row.createCell(1).setCellValue(result.getWorkName());
            row.createCell(2).setCellValue(result.getBetName());
            row.createCell(3).setCellValue(result.getBetAmount());
            row.createCell(4).setCellValue(result.getBetDate());
        }

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            userSheet.autoSizeColumn(i);
        }
    }

    @Override
    public void createWorkSheet(Long id, Sheet workSheet, CellStyle headerStyle) {
        // 创建表头
        Row headerRow = workSheet.createRow(0);
        String[] headers = {"活动名称", "作品名称", "投注总金额"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        List<ActivityBet> list = activityBetMapper.selectList(new QueryWrapper<ActivityBet>()
                .eq("activity_id", id));
        Map<Long, Long> workAmountMap = list.stream()
                .collect(Collectors.groupingBy(
                        ActivityBet::getWorkId,
                        Collectors.summingLong(ActivityBet::getAmount)
                ));
        Activity activity = activityMapper.selectById(list.get(0).getActivityId());

        List<ActivityBetWorkResult> betList = new ArrayList<>();
        for (Long workId : workAmountMap.keySet()) {
            ActivityBetWorkResult betResult = new ActivityBetWorkResult();
            betResult.setActivityName(activity.getName());
            betResult.setWorkName(workMapper.selectById(workId).getTitle());
            betResult.setBetAmount(workAmountMap.get(workId));
            betList.add(betResult);
        }

        // 填充数据
        int rowNum = 1;
        for (ActivityBetWorkResult result : betList) {
            Row row = workSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(result.getActivityName());
            row.createCell(1).setCellValue(result.getWorkName());
            row.createCell(3).setCellValue(result.getBetAmount());
        }

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            workSheet.autoSizeColumn(i);
        }
    }

    @Override
    public Activity getById(Serializable id) {
        Activity activity = super.getById(id);
        fillStatus(activity);
        return activity;
    }

    @Override
    public List<Activity> list() {
        List<Activity> list = super.list();
        list.forEach(this::fillStatus);
        return list;
    }

    @Override
    public List<Activity> list(Wrapper<Activity> queryWrapper) {
        List<Activity> list = super.list(queryWrapper);
        list.forEach(this::fillStatus);
        return list;
    }

    // 不覆盖分页方法，避免与框架泛型签名冲突；分页状态在控制器中填充

    private void fillStatus(Activity activity) {
        if (activity == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = activity.getStartTime();
        LocalDateTime end = activity.getEndTime();
        if (start != null && now.isBefore(start)) {
            activity.setStatus(0);
        } else if (end != null && now.isAfter(end)) {
            activity.setStatus(2);
        } else {
            activity.setStatus(1);
        }
    }
}
