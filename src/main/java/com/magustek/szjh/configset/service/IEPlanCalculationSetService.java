package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanCalculationSet;
import com.magustek.szjh.configset.bean.IEPlanOperationSet;

import java.util.List;

public interface IEPlanCalculationSetService {
    List<IEPlanCalculationSet> save(List<IEPlanCalculationSet> list);
    List<IEPlanCalculationSet> getAll();
    void deleteAll();
    List<IEPlanCalculationSet> getAllFromDatasource() throws Exception;
}
