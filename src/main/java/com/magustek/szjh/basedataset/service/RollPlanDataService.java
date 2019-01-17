package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import com.magustek.szjh.basedataset.entity.RollPlanItemData;
import com.magustek.szjh.basedataset.entity.vo.RollPlanHeaderVO;

import java.util.List;
import java.util.Map;

public interface RollPlanDataService {
    List<RollPlanHeadData> saveHead(List<RollPlanHeadData> list);
    List<RollPlanItemData> saveItem(List<RollPlanItemData> list);
    List<RollPlanHeadData> getAllByVersion(String version);
    void deleteAllByVersion(String version);
    List<RollPlanHeadData> calculateByVersion(String version);
    List<RollPlanHeaderVO> getRollPlanVOByVersionAndHtsno(String version, String htsno);
    List<Map<String, String>> coverToMap(List<RollPlanHeaderVO> list);
    Map<String, String> getContractDataByVersionAndHtsno(String version, String htsno) throws Exception;
}
