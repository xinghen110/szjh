package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;

import java.util.List;

public interface IEPlanSelectValueSetService {
    List<IEPlanSelectValueSet> save(List<IEPlanSelectValueSet> list);
    List<IEPlanSelectValueSet> getAll();
    void deleteAll();
    List<IEPlanSelectValueSet> getAllFromDatasource(String begin, String end, String bukrs) throws Exception;
    List<IEPlanSelectValueSet> fetchData() throws Exception;
}
