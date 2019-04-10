package com.magustek.szjh.report.service;

import com.magustek.szjh.report.bean.Report;
import com.magustek.szjh.report.bean.vo.DateVO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Map;

/**
 * @author hexin
 */
public interface StatisticalReportService {
    /**
     * 销项发票跟踪
     * @param report 版本号及分页信息
     * @return  分页数据
     * @throws Exception    无
     */
    Page<Map<String, String>> getOutputTaxDetailByVersion(Report report) throws Exception;

    /**
     * 根据选择的【起始日期】、【截止日期】，从最新一版月度计划中检索履约待办
     * （item-ctdtp=C）合同，并返回其列表
     * @param dateVO 包含起始日期、截止日期、分页信息
     * @return  分页数据
     * @throws Exception    无
     */
    Page<Map<String, String>> getPendingItemListByDate(DateVO dateVO) throws Exception;
}
