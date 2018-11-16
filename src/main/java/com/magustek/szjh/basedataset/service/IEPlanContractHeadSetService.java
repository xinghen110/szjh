package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.IEPlanContractHeadSet;

import java.util.List;

public interface IEPlanContractHeadSetService {
    List<IEPlanContractHeadSet> save(List<IEPlanContractHeadSet> list);
    List<IEPlanContractHeadSet> getAll();
    void deleteAll();
    List<IEPlanContractHeadSet> getAllFromDatasource(String begin, String end) throws Exception;
}
