package com.bosc.txx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosc.txx.model.Activity;
import com.bosc.txx.dao.ActivityMapper;
import com.bosc.txx.service.IActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return activityMapper.selectOne(new QueryWrapper<Activity>().eq("name", name));
    }
}
