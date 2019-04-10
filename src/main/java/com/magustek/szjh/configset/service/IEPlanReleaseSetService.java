package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanReleaseSet;

import java.util.List;

public interface IEPlanReleaseSetService {
    List<IEPlanReleaseSet> save(List<IEPlanReleaseSet> list);
    List<IEPlanReleaseSet> getAll();
    void deleteAll();
    List<IEPlanReleaseSet> getAllFromDatasource() throws Exception;
}
