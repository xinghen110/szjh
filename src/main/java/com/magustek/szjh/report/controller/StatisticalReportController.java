package com.magustek.szjh.report.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.report.bean.Report;
import com.magustek.szjh.report.bean.vo.DateVO;
import com.magustek.szjh.report.service.StatisticalReportService;
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
 * 统计报表
 *
 * @author hexin*/
@Api("统计报表")
@Slf4j
@RestController
@RequestMapping(value = "/statisticalReport", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class StatisticalReportController {
    private BaseResponse resp;
    private StatisticalReportService statisticalReportService;

    public StatisticalReportController(StatisticalReportService statisticalReportService) {
        this.statisticalReportService = statisticalReportService;
        resp = new BaseResponse();
    }

    @ApiOperation(value="根据version获取【销项发票】数据", notes = "参数：version")
    @RequestMapping("/getOutputTaxDetailByVersion")
    public String getOutputTaxDetailByVersion(@RequestBody Report report) throws Exception{
        Page<Map<String, String>> detail = statisticalReportService.getOutputTaxDetailByVersion(report);
        String userName = ContextUtils.getUserName();
        log.warn("{}根据headerId获取计划明细：{}", userName, JSON.toJSONString(detail));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(detail).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据version获取【销项发票】数据", notes = "参数：version")
    @RequestMapping("/getPendingItemListByDate")
    public String getPendingItemListByDate(@RequestBody DateVO dateVO) throws Exception{
        Page<Map<String, String>> detail = statisticalReportService.getPendingItemListByDate(dateVO);
        //String userName = ContextUtils.getUserName();
        //log.warn("{}根据headerId获取计划明细：{}", userName, JSON.toJSONString(detail));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(detail).setMsg("成功！").toJson();
    }
}
