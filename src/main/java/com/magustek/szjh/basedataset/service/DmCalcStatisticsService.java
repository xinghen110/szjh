package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.DmCalcStatistics;

import java.util.List;
import java.util.Map;

public interface DmCalcStatisticsService {
    List<DmCalcStatistics> save(List<DmCalcStatistics> list);
    void deleteByVersion(String version);
    int statisticByVersion(String version);
    List<Map<String, Object>> getStatisticsByDmartAndVersion(String dmart, String version);
    String getCaval(String version, String dmart, String dmval, String caart, Map<String, String> cache);
    List<DmCalcStatistics> getStatisticsByDmartAndCaartAndVersion(String dmart, String caart, String version);
    List<DmCalcStatistics> getDmCalcChart(String[] versionList, String[] dmvalList, String caart);
}
