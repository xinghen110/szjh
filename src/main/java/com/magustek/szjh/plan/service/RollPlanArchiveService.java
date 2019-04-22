package com.magustek.szjh.plan.service;

import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import com.magustek.szjh.plan.bean.RollPlanItemDataArchive;
import com.magustek.szjh.plan.bean.vo.RollPlanHeadDataArchiveVO;
import com.magustek.szjh.plan.bean.vo.RollPlanItemDataArchiveVO;
import com.magustek.szjh.utils.KeyValueBean;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface RollPlanArchiveService {
    void copyData(PlanHeader header) throws Exception;
    void deleteData(PlanHeader header);
    List<RollPlanHeadDataArchive> getHeadDataArchiveList(PlanHeader header);
    List<RollPlanItemDataArchive> getItemDataArchiveList(PlanHeader header);
    Map<RollPlanHeadDataArchive,List<RollPlanItemDataArchive>> getRollPlanListByPlanIdAndHtsno(Long id, String htsno);

    List<RollPlanHeadDataArchive> getHeadData(String zbart, String dmval, String dtval, Long planHeadId, boolean firstMonth, boolean lastMonth);

    List<RollPlanHeadDataArchive> getHeadDataByPlanHeadIdAndDmvalAndZbart(String zbart, String dmval, Long planHeadId);
    List<RollPlanItemDataArchive> getItemDataByHeadIdAndImnum(List<Long> headIdList, List<String>imnumList);

    void saveItemList(List<RollPlanItemDataArchive> changedList);

    /**
     * 根据计划ID，时间范围，获取所有相关Item列表
     * @param id    计划ID
     * @param start 起始日期
     * @param end   截止日期
     * @return      相关Item列表
     */
    List<RollPlanItemDataArchiveVO> getItemListByPlanHeaderIdAndStartEndDate(Long id, String start, String end);

    /**
     * 根据计划ID获取所有滚动计划head及其Item
     * @param id    计划ID
     * @return      相关Item列表
     */
    List<RollPlanHeadDataArchiveVO> getListByPlanHeaderId(Long id);
}
