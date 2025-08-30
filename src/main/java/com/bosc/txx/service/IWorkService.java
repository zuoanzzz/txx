package com.bosc.txx.service;

import com.bosc.txx.model.Work;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosc.txx.model.dto.work.ListAllWorkReq;
import com.bosc.txx.model.dto.work.ListAllWorkResp;

/**
 * <p>
 * 活动作品表 服务类
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */
public interface IWorkService extends IService<Work> {

    ListAllWorkResp listAll(ListAllWorkReq req);
}
