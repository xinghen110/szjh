package com.magustek.szjh.plan.service;

import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import com.magustek.szjh.plan.bean.RollPlanItemDataArchive;
import com.magustek.szjh.plan.bean.vo.RollPlanHeadDataArchiveVO;
import com.magustek.szjh.plan.bean.vo.RollPlanItemDataArchiveVO;
import java.util.List;

public interface RollPlanArchiveService {
    void copyData(PlanHeader header) throws Exception;
    void deleteData(PlanHeader header);
    List<RollPlanHeadDataArchive> getHeadDataArchiveList(Long PlanHeadId);
    List<RollPlanHeadDataArchive> getHeadData(String zbart, String dmval, String dtval, Long planHeadId, boolean firstMonth, boolean lastMonth);
    List<RollPlanHeadDataArchive> getHeadDataByPlanHeadIdAndDmvalAndZbart(String zbart, String dmval, Long planHeadId);
    List<RollPlanHeadDataArchive> getHeadDataByPlanHeadIdAndZbart(Long planHeadId, String zbart);

    List<RollPlanItemDataArchive> getItemDataByHeadIdAndImnum(List<Long> headIdList, List<String>imnumList);
    List<RollPlanItemDataArchive> getItemDataByPlanHeadId(Long PlanHeadId);

    void saveItemList(List<RollPlanItemDataArchive> changedList);
    void saveHeadList(List<RollPlanHeadDataArchive> changedList);

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
     * @param PlanHeadId    计划ID
     * @return      相关Item列表
     */
    List<RollPlanHeadDataArchiveVO> getListByPlanHeaderId(Long PlanHeadId);
}
