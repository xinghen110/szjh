package com.magustek.szjh.plan.service;

import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.PlanItem;
import com.magustek.szjh.plan.bean.vo.PlanItemVO;
import com.magustek.szjh.utils.KeyValueBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PlanItemService {
    PlanItem[] save(PlanItem[] items) throws Exception;
    List<PlanItem> save(List<PlanItem> list);
    List<PlanItem> getListByHeaderId(PlanItemVO vo) throws Exception;
    List<PlanItem> delete(List<PlanItem> list);
    void deleteByHeaderId(Long headerId);
    List<PlanItem> coverVOToList(PlanItemVO vo) throws Exception;
    PlanItemVO coverListToVO(List<PlanItem> list, String zValue) throws Exception;
    List<Map<String, String>> coverListToMap(List<PlanItem> list) throws Exception;
    Map<String, BigDecimal> getZBValByHeaderId(Long headerId);
    //初始化明细表数据
    List<PlanItem> initItemDataByConfig(IEPlanReportHeadVO config, Long headerId) throws Exception;
    //初始化统计数据
    void initCalcData(List<PlanItem> itemList, IEPlanReportHeadVO config, PlanHeader planHeader) throws Exception;

    //根据指标分组统计计划的zbval值
    ArrayList<KeyValueBean> getZbList(Long headerId, String rptyp);
}
