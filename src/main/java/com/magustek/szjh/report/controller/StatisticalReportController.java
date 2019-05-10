package com.magustek.szjh.report.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.configset.bean.IEPlanCalculationSet;
import com.magustek.szjh.configset.service.IEPlanCalculationSetService;
import com.magustek.szjh.report.bean.vo.ReportVO;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private IEPlanCalculationSetService iePlanCalculationSetService;

    public StatisticalReportController(StatisticalReportService statisticalReportService, IEPlanCalculationSetService iePlanCalculationSetService) {
        this.statisticalReportService = statisticalReportService;
        this.iePlanCalculationSetService = iePlanCalculationSetService;
        resp = new BaseResponse();
    }

    @ApiOperation(value="根据version获取【销项发票】数据", notes = "参数：version")
    @RequestMapping("/getOutputTaxDetailByVersion")
    public String getOutputTaxDetailByVersion(@RequestBody ReportVO reportVO) throws Exception{
        Page<Map<String, String>> detail = statisticalReportService.getOutputTaxDetailByVersion(reportVO);
        String userName = ContextUtils.getUserName();
        log.warn("{}根据version获取【销项发票】数据:{}", userName, JSON.toJSONString(detail));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(detail).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据version获取【履约待办工作】数据", notes = "参数：version")
    @RequestMapping("/getPendingItemListByDate")
    public String getPendingItemListByDate(@RequestBody DateVO dateVO) throws Exception{
        List<Map<String, String>> detailList = statisticalReportService.getPendingItemListByDate(dateVO);
        Page<Map<String, String>> page = ClassUtils.constructPage(dateVO, detailList);
        String userName = ContextUtils.getUserName();
        log.warn("{}根据version获取【履约待办工作】数据：{}", userName, JSON.toJSONString(page));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(page).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据计划id-id，对比数据版本（日期）-version，能力值类型-caart，获取【计划履行报表】", notes = "参数：id、version")
    @RequestMapping("/getExecutionByPlan")
    public String getExecutionByPlan(@RequestBody ReportVO reportVO) throws Exception{
        //如果【历史能力值类型】为空，则返回所有的能力值类型
        if(ClassUtils.isEmpty(reportVO.getCaartList())){
            reportVO.setCaartList(iePlanCalculationSetService
                    .getAll()
                    .stream()
                    .map(IEPlanCalculationSet::getCaart)
                    .collect(Collectors.toList()));
        }
        List<Map<String, String>> list = statisticalReportService.getExecutionByPlan(reportVO.getId(), reportVO.getVersion(), reportVO.getCaartList());
        String userName = ContextUtils.getUserName();
        Page page = reportVO.initPageImpl(list);
        log.warn("{}根据计划id-id，对比数据版本（日期）-version，能力值类型-caart，获取【计划履行报表】：{}", userName, JSON.toJSONString(page));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(page).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据计划id-id，对比数据版本（日期）-version，部门编码-dpnum，能力值类型-caart，获取【计划履行报表】", notes = "参数：id、version")
    @RequestMapping("/getExecutionByPlanAndDpnum")
    public String getExecutionByPlanAndDpnum(@RequestBody ReportVO reportVO) throws Exception{
        List<Map<String, String>> list = statisticalReportService.getExecutionByPlanAndDpnum(
                reportVO.getId(),
                reportVO.getVersion(),
                reportVO.getDpnum(),
                reportVO.getCaart());
        list = reportVO.filter(list);
        Page page = reportVO.initPageImpl(list);
        String userName = ContextUtils.getUserName();
        log.warn("{}根据计划id-id，对比数据版本（日期）-version，部门编码-dpnum，能力值类型-caart，获取【计划履行报表】：{}", userName, JSON.toJSONString(page));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(page).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据年度计划id-id，指标值-zbart，获取年度计划完成率", notes = "参数：id、zbart")
    @RequestMapping("/compareMRAndAR")
    public String compareMRAndAR(@RequestBody ReportVO reportVO) throws Exception{
        List<Map<String, String>> list = statisticalReportService.compareMRAndAR(reportVO.getId(), reportVO.getZbart());
        list = reportVO.filter(list);
        Page page = reportVO.initPageImpl(list);
        String userName = ContextUtils.getUserName();
        log.warn("{}根据年度计划id-id，指标值-zbart，获取年度计划完成率：{}", userName, JSON.toJSONString(page));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(page).setMsg("成功！").toJson();
    }
}
