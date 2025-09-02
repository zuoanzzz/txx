package com.bosc.txx.controller;

import com.bosc.txx.common.CommonResult;
import com.bosc.txx.model.dto.ActivityBet.ActivityBetReq;
import com.bosc.txx.service.IActivityBetService;
import com.bosc.txx.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 活动投注记录表 前端控制器
 * </p>
 *
 * @author code generator
 * @since 2025-08-25
 */

@RestController
@RequestMapping("/activityBet")
public class ActivityBetController {

    @Autowired
    private IActivityBetService activityBetService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/bet")
    public CommonResult<Boolean> bet(@RequestBody ActivityBetReq activityBetReq, HttpServletRequest request){
        String token = jwtUtil.extractTokenFromHeader(request.getHeader("Authorization"));
        Boolean ok = activityBetService.bet(activityBetReq, jwtUtil.getUserIdFromToken(token));
        return ok ? CommonResult.success() : CommonResult.failed();
    }

}
