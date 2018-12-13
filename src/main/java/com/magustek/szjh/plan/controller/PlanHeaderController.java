package com.magustek.szjh.plan.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.vo.PlanHeaderVO;
import com.magustek.szjh.plan.service.PlanHeaderService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 计划抬头
 * */
@Api("计划抬头")
@Slf4j
@RestController
@RequestMapping(value = "/planHeader", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class PlanHeaderController {
    private BaseResponse resp;
    private PlanHeaderService planHeaderService;

    public PlanHeaderController(PlanHeaderService planHeaderService) {
        this.planHeaderService = planHeaderService;
        resp = new BaseResponse();
    }

    @ApiOperation(value="保存计划抬头（保存抬头后，会自动生成报表布局并初始化数据）", notes = "参数：参考PlanHeader结构")
    @RequestMapping("/save")
    public String savePlanHeader(@RequestBody PlanHeader header) throws Exception {
        header = planHeaderService.save(header);
        String userName = ContextUtils.getUserName();
        log.warn("{}保存计划抬头：{}", userName, JSON.toJSONString(header));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(header).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据id删除计划", notes = "参数：id")
    @RequestMapping("/deletePlanHeader")
    public String deletePlanHeader(@RequestBody PlanHeader header) {
        header = planHeaderService.delete(header);
        String userName = ContextUtils.getUserName();
        log.warn("{}删除计划：{}", userName, JSON.toJSONString(header));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(header).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据id获取计划", notes = "参数：id")
    @RequestMapping("/getPlanHeaderById")
    public String getPlanHeaderById(@RequestBody PlanHeader header) throws Exception {
        header = planHeaderService.getById(header);
        String userName = ContextUtils.getUserName();
        log.warn("{}获取计划：{}", userName, JSON.toJSONString(header));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(header).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据公司获取计划列表", notes = "参数：rporg、rptyp")
    @RequestMapping("/getPlanHeaderByBukrs")
    public String getPlanHeaderByBukrs(@RequestBody PlanHeaderVO vo) throws Exception {
        Page<Map<String, String>> listByBukrs = planHeaderService.getListByBukrs(vo, vo.getPageRequest());
        String userName = ContextUtils.getUserName();
        log.warn("{}获取计划：{}", userName, JSON.toJSONString(listByBukrs));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(listByBukrs).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据计划抬头ID代码获取报表布局信息，参数：1、id。")
    @RequestMapping("/getLayoutByHeaderId")
    public String getLayoutByHeaderId(@RequestBody PlanHeaderVO vo) {
        IEPlanReportHeadVO config = planHeaderService.getLayoutByHeaderId(vo.getId());
        log.warn("根据计划抬头ID代码获取报表布局信息：{}", JSON.toJSONString(config));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(config).setMsg("成功！").toJson();
    }
}
