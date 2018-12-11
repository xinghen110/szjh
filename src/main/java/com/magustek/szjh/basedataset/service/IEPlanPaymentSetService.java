package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.IEPlanPaymentSet;

import java.util.List;

public interface IEPlanPaymentSetService {
    List<IEPlanPaymentSet> save(List<IEPlanPaymentSet> list);
    List<IEPlanPaymentSet> getAllByVersion(String version);
    void deleteAllByVersion(String version);
    void deleteAllByVersionAndBukrs(String version, String bukrs);

    List<IEPlanPaymentSet> fetchData() throws Exception;

}
