package com.magustek.szjh.chart.service;

import com.magustek.szjh.chart.bean.vo.DmCalcVO;

import java.util.List;
import java.util.Map;

public interface ChartService {
    Map<String, Map<String, List<Map<String, String>>>> dmCalc(DmCalcVO vo);
}
