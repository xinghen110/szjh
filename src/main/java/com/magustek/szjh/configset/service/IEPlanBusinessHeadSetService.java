package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanBusinessHeadSet;
import com.magustek.szjh.configset.bean.vo.IEPlanBusinessHeadSetVO;

import java.util.List;

public interface IEPlanBusinessHeadSetService {
    List<IEPlanBusinessHeadSet> save(List<IEPlanBusinessHeadSet> list);
    List<IEPlanBusinessHeadSet> getAll();
    List<IEPlanBusinessHeadSet> getAllByBukrsAndRptyp(String bukrs, String rptyp);
    List<IEPlanBusinessHeadSetVO> getAllVOByBukrsAndRptyp(String bukrs, String rptyp);
    void deleteAll();
    List<IEPlanBusinessHeadSet> getAllFromDatasource() throws Exception;
}
