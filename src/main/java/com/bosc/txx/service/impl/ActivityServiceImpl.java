package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosc.txx.dao.ActivityMapper;
import com.bosc.txx.model.Activity;
import com.bosc.txx.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public Activity getByName(String name) {
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("name", name));
        fillStatus(activity);
        return activity;
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
