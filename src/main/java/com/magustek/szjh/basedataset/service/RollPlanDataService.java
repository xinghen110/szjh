package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.RollPlanHeadData;

import java.util.List;

public interface RollPlanDataService {
    List<RollPlanHeadData> save(List<RollPlanHeadData> list);
    List<RollPlanHeadData> getAllByVersion(String version);
    void deleteAllByVersion(String version);
    List<RollPlanHeadData> calculateByVersion(String version);
}
