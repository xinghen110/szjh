package com.magustek.szjh.report.service;

import com.magustek.szjh.report.bean.vo.ReportVO;
import com.magustek.szjh.report.bean.vo.DateVO;
import com.magustek.szjh.utils.base.BasePage;
import org.springframework.data.domain.Page;

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
    Page<Map<String, String>> getPendingItemListByDate(DateVO dateVO) throws Exception;

    /**
     * 根据计划id-id，对比数据版本（日期）-version，能力值类型-caart，获取【计划履行报表】
     * @param id 计划id
     * @param version 对比数据版本
     * @param caart 历史能力值类型
     * @return 计划履行报表
     * @throws Exception    无
     */
    List<Map<String, String>> getExecutionByPlan(Long id, String version, String caart) throws Exception;
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
}
