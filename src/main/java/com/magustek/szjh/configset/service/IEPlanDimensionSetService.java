package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanDimensionSet;

import java.util.List;
import java.util.Map;

public interface IEPlanDimensionSetService {
    List<IEPlanDimensionSet> save(List<IEPlanDimensionSet> list);
    List<IEPlanDimensionSet> getAll();
    Map<String, IEPlanDimensionSet> getMappedList();
    void deleteAll();
    List<IEPlanDimensionSet> getAllFromDatasource() throws Exception;
}
