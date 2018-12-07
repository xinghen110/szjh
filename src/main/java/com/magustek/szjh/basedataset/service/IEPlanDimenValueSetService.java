package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;

import java.util.List;

public interface IEPlanDimenValueSetService {
    List<IEPlanDimenValueSet> save(List<IEPlanDimenValueSet> list);
    List<IEPlanDimenValueSet> getAll();
    void deleteAll();
    List<IEPlanDimenValueSet> getAllFromDatasource(String begin, String end, String bukrs) throws Exception;
    List<IEPlanDimenValueSet> fetchData() throws Exception;
}
