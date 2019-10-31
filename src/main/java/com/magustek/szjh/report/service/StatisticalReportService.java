package com.magustek.szjh.report.service;

import com.magustek.szjh.configset.bean.IEPlanScreenHeadSet;
import com.magustek.szjh.report.bean.vo.ReportVO;
import com.magustek.szjh.report.bean.vo.DateVO;
import com.magustek.szjh.utils.base.BasePage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author hexin
 */
public interface StatisticalReportService {
    /**
     * 销项发票跟踪
     * @param reportVO 版本号及分页信息
     * @return  分页数据
     * @throws Exception    无
     */
    Page<Map<String, String>> getOutputTaxDetailByVersion(ReportVO reportVO) throws Exception;

    /**
     * 根据选择的【起始日期】、【截止日期】，从最新一版月度计划中检索履约待办
     * （item-ctdtp=C）合同，并返回其列表
     * @param dateVO 包含起始日期、截止日期、分页信息
     * @return  分页数据
     * @throws Exception    无
     */
    List<Map<String, String>> getPendingItemListByDate(DateVO dateVO) throws Exception;

    /**
     * 根据计划id-id，对比数据版本（日期）-version，能力值类型-caart，获取【计划履行报表】
     * @param id 计划id
     * @param version 对比数据版本
     * @param caart 历史能力值类型
     * @return 计划履行报表
     * @throws Exception    无
     */
    List<Map<String, String>> getExecutionByPlan(Long id, String version, List<String> caart) throws Exception;
    /**
     * 根据计划id-id，对比数据版本（日期）-version，部门编码-dpnum，能力值类型-caart，获取【计划履行报表】
     * @param id 计划id
     * @param version 对比数据版本
     * @param caart 历史能力值类型
     * @param dpnum 部门编码
     * @return 计划履行报表
     * @throws Exception    无
     */
    List<Map<String, String>> getExecutionByPlanAndDpnum(Long id, String version, String dpnum, String caart) throws Exception;

    /**
     * 年度、月度计划对比
     * @param arId 年度计划id
     * @param zbart 对比指标
     * @return 年度、月度计划对比
     */
    List<Map<String, String>> compareMRAndAR(Long arId, String zbart) throws Exception;

    /**
     * 销项发票跟踪数据导出为Excel
     * @param rptyp
     * @param hview
     * @throws IOException
     */
    HSSFWorkbook exportTaxDetailByExcel(String rptyp, String hview) throws Exception;
}
