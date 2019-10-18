package com.magustek.szjh.report.controller;

import com.magustek.szjh.configset.bean.IEPlanScreenHeadSet;
import com.magustek.szjh.configset.service.IEPlanCalculationSetService;
import com.magustek.szjh.report.service.StatisticalReportService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 统计报表
 *
 * @author hexin*/
@Api("统计报表导出")
@Slf4j
@RestController
@RequestMapping(value = "/statisticalReport", method = RequestMethod.GET, produces = ClassUtils.HTTP_HEADER)
public class ExportStatisticalReportController {
    private BaseResponse resp;
    private StatisticalReportService statisticalReportService;

    public ExportStatisticalReportController(StatisticalReportService statisticalReportService, IEPlanCalculationSetService iePlanCalculationSetService) {
        this.statisticalReportService = statisticalReportService;
        resp = new BaseResponse();
    }
    @ApiOperation(value="根据bukrs、rptyp、hview屏幕配置导出【销项发票】数据excel", notes = "参数：bukrs、rptyp、hview")
    @RequestMapping("/exportTaxDetailByExcel")
    public String exportTaxDetailByExcel(HttpServletResponse response, @RequestParam(value = "rptyp") String rptyp, @RequestParam(value = "hview") String hview ) throws Exception {
        statisticalReportService.exportTaxDetailByExcel(response, rptyp, hview);
        return resp.setStateCode(BaseResponse.SUCCESS).setMsg("成功！").toJson();
    }
}
