package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.IEPlanBusinessItemSet;
import com.magustek.szjh.configset.bean.vo.IEPlanBusinessItemSetVO;

import java.util.List;
import java.util.Map;

public interface IEPlanBusinessItemSetService {
    List<IEPlanBusinessItemSet> save(List<IEPlanBusinessItemSet> list);
    List<IEPlanBusinessItemSet> getAll();
    List<IEPlanBusinessItemSetVO> getAllVO();
    Map<String, List<IEPlanBusinessItemSet>> getMap();
    void deleteAll();
    List<IEPlanBusinessItemSet> getAllFromDatasource() throws Exception;

    List<IEPlanBusinessItemSet> getAllByCaart(String caart);
    List<IEPlanBusinessItemSet> getAllByCaartIn(List<String> caartList);

    List<IEPlanBusinessItemSet> getNextItemList(String imnum);
}
