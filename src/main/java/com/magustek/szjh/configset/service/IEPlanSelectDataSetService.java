package com.magustek.szjh.configset.service;


import com.magustek.szjh.configset.bean.IEPlanSelectDataSet;

import java.util.List;
import java.util.Map;

public interface IEPlanSelectDataSetService {
    List<IEPlanSelectDataSet> save(List<IEPlanSelectDataSet> list);
    List<IEPlanSelectDataSet> getAll();
    Map<String, IEPlanSelectDataSet> getMappedList();
    void deleteAll();
    List<IEPlanSelectDataSet> getAllFromDatasource() throws Exception;
}
