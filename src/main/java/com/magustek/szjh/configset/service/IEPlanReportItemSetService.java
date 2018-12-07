package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanReportItemSet;

import java.util.List;

public interface IEPlanReportItemSetService {
    List<IEPlanReportItemSet> save(List<IEPlanReportItemSet> list);
    List<IEPlanReportItemSet> getAll();
    void deleteAll();
    List<IEPlanReportItemSet> getAllFromDatasource() throws Exception;
    List<IEPlanReportItemSet> getByBukrsAndRptyp(String bukrs, String rptyp) throws Exception;
}
