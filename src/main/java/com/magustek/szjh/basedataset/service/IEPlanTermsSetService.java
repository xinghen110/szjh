package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.IEPlanTermsSet;

import java.util.List;

public interface IEPlanTermsSetService {
    List<IEPlanTermsSet> save(List<IEPlanTermsSet> list);
    List<IEPlanTermsSet> getAllByVersion(String version);
    void deleteAllByVersion(String version);
    List<IEPlanTermsSet> fetchData() throws Exception;
}
