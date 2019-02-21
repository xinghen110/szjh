package com.magustek.szjh.plan.service;

import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.vo.PlanHeaderVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PlanHeaderService {
    PlanHeader save(PlanHeader header) throws Exception;
    PlanHeader delete(PlanHeader header);
    PlanHeader getById(PlanHeader header);
    Page<Map<String, String>> getListByBukrs(PlanHeaderVO vo, Pageable pageable) throws Exception;
    //获取布局信息
    IEPlanReportHeadVO getLayoutByHeaderId(Long headerId);
    //查看部门合同计划
    List<Map<String, String>> getHtsnoList(String zbart, String dmval, String dtval, Long planHeadId, Pageable pageable) throws Exception;

    List<Map<String, Object>> getCavalByPlanHeadIdAndCaartAndDmart(Long planHeadId, String caart, String dmart, String zbart);
    int updateCavalByPlanHeadIdAndCaartAndDmartAndDmval(Long planHeadId, String caart, String dmart, String dmval, String zbart, Integer caval);
}
