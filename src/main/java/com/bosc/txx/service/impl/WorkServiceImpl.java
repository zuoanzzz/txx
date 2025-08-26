package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosc.txx.dao.ActivityBetMapper;
import com.bosc.txx.dao.ActivityMapper;
import com.bosc.txx.model.Activity;
import com.bosc.txx.model.ActivityBet;
import com.bosc.txx.model.Work;
import com.bosc.txx.dao.WorkMapper;
import com.bosc.txx.model.dto.work.ListAllWorkReq;
import com.bosc.txx.model.dto.work.ListAllWorkResp;
import com.bosc.txx.model.dto.work.WorkDto;
import com.bosc.txx.service.IWorkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 活动作品表 服务实现类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements IWorkService {

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    WorkMapper workMapper;

    @Autowired
    ActivityBetMapper activityBetMapper;

    @Override
    public ListAllWorkResp listAll(ListAllWorkReq req) {
        ListAllWorkResp listAllWorkResp = new ListAllWorkResp();
        List<Work> workList = workMapper.selectList(new QueryWrapper<Work>()
                .eq("activity_id", req.getActivityId()));
        List<WorkDto> workDtoList = new ArrayList<>();

        if (workList != null) {
            for (Work work : workList) {
                WorkDto workDto = new WorkDto();
                BeanUtils.copyProperties(work, workDto);
                workDtoList.add(workDto);
            }
        }
        listAllWorkResp.setWorkList(workDtoList);

        if (Objects.nonNull(req.getAccountId())) {
            List<ActivityBet> activityBets = activityBetMapper.selectList(new QueryWrapper<ActivityBet>()
                    .eq("activity_id", req.getActivityId()));
            Long freeCredit = req.getFreeCredit();
            for (ActivityBet activityBet : activityBets) {
                freeCredit -= activityBet.getUsedFreeAmount();
            }
            listAllWorkResp.setFreeCredit(freeCredit);
        } else {
            for (WorkDto workDto : workDtoList) {
                List<ActivityBet> activityBets = activityBetMapper.selectList(new QueryWrapper<ActivityBet>()
                        .eq("work_id", workDto.getId()));
                Long amount = 0L;
                for (ActivityBet activityBet : activityBets) {
                    amount += activityBet.getAmount();
                }

                workDto.setAmount(amount);
            }
        }

        return listAllWorkResp;
    }
}
