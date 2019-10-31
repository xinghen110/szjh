package com.magustek.szjh.report.controller;

import com.magustek.szjh.configset.bean.vo.IEPlanScreenVO;
import com.magustek.szjh.configset.service.IEPlanScreenService;
import com.magustek.szjh.plan.service.PlanHeaderService;
import com.magustek.szjh.report.service.StatisticalReportService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * 统计报表导出
 *
 * @author hexin*/
@Api("统计报表导出")
@Slf4j
@RestController
@RequestMapping(value = "/statisticalReport", method = RequestMethod.GET, produces = ClassUtils.HTTP_HEADER)
public class ExportStatisticalReportController {
    private BaseResponse resp;
    private StatisticalReportService statisticalReportService;
    private PlanHeaderService planHeaderService;
    private IEPlanScreenService iePlanScreenService;

    public ExportStatisticalReportController(StatisticalReportService statisticalReportService, PlanHeaderService planHeaderService, IEPlanScreenService iePlanScreenService) {
        this.statisticalReportService = statisticalReportService;
        this.planHeaderService = planHeaderService;
        this.iePlanScreenService = iePlanScreenService;
        resp = new BaseResponse();
    }

    @ApiOperation(value="根据bukrs、rptyp、hview屏幕配置导出【销项发票】数据excel", notes = "参数：bukrs、rptyp、hview")
    @RequestMapping("/exportTaxDetailByExcel")
    public void exportTaxDetailByExcel(HttpServletResponse response, @RequestParam(value = "rptyp") String rptyp, @RequestParam(value = "hview") String hview ) throws Exception {
        IEPlanScreenVO iePlanScreenVO = iePlanScreenService.findHeadByBukrsAndRptypAndHview(ContextUtils.getCompany().getOrgcode(), rptyp, hview);
        HSSFWorkbook workbook = statisticalReportService.exportTaxDetailByExcel(rptyp, hview);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(iePlanScreenVO.getHdtxt() + ".xls", "UTF-8"));
        workbook.write(response.getOutputStream());
    }

    @ApiOperation(value="根据zbart、planHeadId导出【所有部门计划】数据excel", notes = "参数：zbart、planHeadId")
    @RequestMapping("/exportAllHtsnoListByExcel")
    public String exportAllHtsnoListByExcel(HttpServletResponse response, @RequestParam(value = "zbart") String zbart, @RequestParam(value = "headerId") Long headerId ) throws Exception {
        planHeaderService.exportAllHtsnoListByExcel(response, zbart, headerId);
        return resp.setStateCode(BaseResponse.SUCCESS).setMsg("成功！").toJson();
    }
}
