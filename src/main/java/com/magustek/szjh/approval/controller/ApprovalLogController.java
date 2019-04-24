package com.magustek.szjh.approval.controller;

import com.magustek.szjh.approval.bean.ApprovalLog;
import com.magustek.szjh.approval.service.ApprovalLogService;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 审批日志
 * */
@Api("审批日志")
@Slf4j
@RestController
@RequestMapping(value = "/approvalLog", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class ApprovalLogController {
    private ApprovalLogService approvalLogService;
    private BaseResponse resp;

    public ApprovalLogController(ApprovalLogService approvalLogService) {
        this.approvalLogService = approvalLogService;
        resp = new BaseResponse();
    }

    @ApiOperation(value="获取该计划审批日志", notes = "参数：id")
    @RequestMapping("/getApprovalLogList")
    public String getApprovalLogList(@RequestBody PlanHeader planHeader){
        List<ApprovalLog> list = approvalLogService.getAllLogsByHeaderId(planHeader.getId());
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

}
