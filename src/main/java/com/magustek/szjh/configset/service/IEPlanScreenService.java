package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanScreenHeadSet;
import com.magustek.szjh.configset.bean.IEPlanScreenItemSet;
import com.magustek.szjh.configset.bean.vo.IEPlanScreenVO;

import java.util.List;

public interface IEPlanScreenService {
    List<IEPlanScreenHeadSet> saveHead(List<IEPlanScreenHeadSet> list);
    List<IEPlanScreenItemSet> saveItem(List<IEPlanScreenItemSet> list);

    IEPlanScreenVO findHeadByBukrsAndRptypAndHview(String bukrs, String rptyp, String hview);

    void deleteAll();
    void getAllFromDatasource() throws Exception;

    List<IEPlanScreenItemSet> getItemListByIntfa(String intfa);
}
