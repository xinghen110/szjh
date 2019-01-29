package com.magustek.szjh.plan.service;

import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import com.magustek.szjh.plan.bean.RollPlanItemDataArchive;

import java.util.List;

public interface RollPlanArchiveService {
    void copyData(PlanHeader header) throws Exception;
    void deleteData(PlanHeader header);
    List<RollPlanHeadDataArchive> getHeadDataArchiveList(PlanHeader header);
    List<RollPlanItemDataArchive> getItemDataArchiveList(PlanHeader header);

    List<RollPlanHeadDataArchive> getHeadData(String zbart, String dmval, String dtval, Long planHeadId);
}
