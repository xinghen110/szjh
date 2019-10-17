package com.magustek.szjh.plan.service;

import com.magustek.szjh.configset.bean.IEPlanScreenHeadSet;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.vo.PlanHeaderVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author hexin
 */
public interface PlanHeaderService {
    PlanHeader save(PlanHeader header) throws Exception;
    PlanHeader delete(PlanHeader header);
    PlanHeader getById(Long id);
    Page<Map<String, String>> getListByBukrs(PlanHeaderVO vo, Pageable pageable) throws Exception;
    //获取布局信息
    IEPlanReportHeadVO getLayoutByHeaderId(Long headerId);
    //查看部门合同计划
    List<Map<String, String>> getHtsnoList(String zbart, String dmval, String dtval, Long planHeadId, Pageable pageable) throws Exception;
    //部门合同计划金额合计
    Map<String, String> getTotalAmountHtsnoList(PlanHeaderVO vo) throws Exception;

    List<Map<String, Object>> getCavalByPlanHeadIdAndCaartAndDmart(Long planHeadId, String caart, String dmart, String zbart);
    int updateCavalByPlanHeadIdAndCaartAndDmartAndDmval(Long planHeadId, String caart, String dmart, String dmval, String zbart, Integer caval) throws Exception;

    void updateCavalByPlanHeadIdAndZbartAndWears(Long planHeadId, String zbart, BigDecimal wears) throws Exception;
    Map<String, String> getCavalByPlanHeadId(Long planHeadId, String zbart) throws Exception;

    /**
     * 获取系统中ckdate最新的月度计划
     * @return 月度计划
     */
    PlanHeader getLastMRPlan();

    //审批流程(提交/同意/驳回)
    PlanHeader approvalProcess(PlanHeaderVO vo) throws Exception;

    //获取审批界面
    PlanHeader getApprovalPage(PlanHeaderVO vo) throws Exception;

    List<PlanHeader> getByJhvalContains(String jhval);

    /**
     * 根据月度计划ID，下达计划并回传CM系统
     * @param id 月度计划id
     */
    boolean issuePlan(Long id);
}
