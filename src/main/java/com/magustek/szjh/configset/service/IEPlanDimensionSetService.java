package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanDimensionSet;

import java.util.List;

public interface IEPlanDimensionSetService {
    List<IEPlanDimensionSet> save(List<IEPlanDimensionSet> list);
    List<IEPlanDimensionSet> getAll();
    void deleteAll();
    List<IEPlanDimensionSet> getAllFromDatasource() throws Exception;
}
