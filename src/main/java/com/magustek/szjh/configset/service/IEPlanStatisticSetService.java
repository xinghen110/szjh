package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanStatisticSet;

import java.util.List;

public interface IEPlanStatisticSetService {
    List<IEPlanStatisticSet> save(List<IEPlanStatisticSet> list);
    List<IEPlanStatisticSet> getAll();
    List<IEPlanStatisticSet> getAllByBukrsAndRptyp(String bukrs, String rptyp);
    void deleteAll();
    List<IEPlanStatisticSet> getAllFromDatasource() throws Exception;
}
