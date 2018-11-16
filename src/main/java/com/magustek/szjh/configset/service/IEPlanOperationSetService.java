package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanOperationSet;

import java.util.List;

public interface IEPlanOperationSetService {
    List<IEPlanOperationSet> save(List<IEPlanOperationSet> list);
    List<IEPlanOperationSet> getAll();
    void deleteAll();
    List<IEPlanOperationSet> getAllFromDatasource() throws Exception;
}
