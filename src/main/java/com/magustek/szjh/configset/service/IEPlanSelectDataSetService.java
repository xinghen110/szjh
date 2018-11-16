package com.magustek.szjh.configset.service;


import com.magustek.szjh.configset.bean.IEPlanSelectDataSet;

import java.util.List;

public interface IEPlanSelectDataSetService {
    List<IEPlanSelectDataSet> save(List<IEPlanSelectDataSet> list);
    List<IEPlanSelectDataSet> getAll();
    void deleteAll();
    List<IEPlanSelectDataSet> getAllFromDatasource() throws Exception;
}
