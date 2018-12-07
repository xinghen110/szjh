package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanReportHeadSet;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.user.bean.CompanyModel;

import java.util.List;

public interface IEPlanReportHeadSetService {
    List<IEPlanReportHeadSet> save(List<IEPlanReportHeadSet> list);
    List<IEPlanReportHeadSet> getAll();
    List<IEPlanReportHeadSet> getAllByRptyp(String rptyp);
    void deleteAll();
    List<IEPlanReportHeadSet> getAllFromDatasource() throws Exception;
    IEPlanReportHeadVO getReportConfigByBukrs(String bukrs, String rptyp, String orgdp, String rpdat) throws Exception;
    IEPlanReportHeadVO getReportConfigByBukrs(String bukrs, String rptyp) throws Exception;
    List<CompanyModel> getBukrsList();
}
