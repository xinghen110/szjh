package com.magustek.szjh.chart.service.impl;

import com.magustek.szjh.basedataset.entity.DmCalcStatistics;
import com.magustek.szjh.basedataset.service.DmCalcStatisticsService;
import com.magustek.szjh.chart.bean.vo.DmCalcVO;
import com.magustek.szjh.chart.service.ChartService;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.KeyValueBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("ChartService")
public class ChartServiceImpl implements ChartService {
    private DmCalcStatisticsService dmCalcStatisticsService;
    private OrganizationSetService organizationSetService;

    public ChartServiceImpl(DmCalcStatisticsService dmCalcStatisticsService, OrganizationSetService organizationSetService) {
        this.dmCalcStatisticsService = dmCalcStatisticsService;
        this.organizationSetService = organizationSetService;
    }

    @Override
    public Map<String, Map<String, List<Map<String, String>>>> dmCalc(DmCalcVO vo){
        LocalDate start = LocalDate.parse(vo.getStartDate());
        LocalDate end = LocalDate.parse(vo.getEndDate());
        ArrayList<String> versionList = new ArrayList<>();
        Assert.isTrue(!start.isAfter(end),"结束日期【"+start.toString()+"】早于起始日期【"+end.toString()+"】");
        //获取范围内所有日期
        while (end.isAfter(start)){
            versionList.add(start.toString());
            start = start.plusDays(1);
        }

        //获取组织机构列表（用来获取组织机构名称）
        Map<String, KeyValueBean> orgMap = organizationSetService.orgKeyValue();
        Map<String, Map<String, List<Map<String, String>>>> dmCalcMap = new TreeMap<>();
        //获取数据，根据历史能力值分组
        Map<String, List<DmCalcStatistics>> caartMap = dmCalcStatisticsService.
                getDmCalcChart(versionList.toArray(new String[0]), vo.getDmval(), vo.getCaart())
                .stream()
                .collect(Collectors.groupingBy(DmCalcStatistics::getCaart));
        //返回数据根据历史能力值分组
        caartMap.forEach((caart, caartList)->{
            Map<String, List<DmCalcStatistics>> dmMap = caartList.stream().collect(Collectors.groupingBy(DmCalcStatistics::getVersion));
            Map<String, List<Map<String, String>>> versionMap = new TreeMap<>();
            //根据日期分组
            dmMap.forEach((k,v)->{
                List<Map<String, String>> list = new ArrayList<>();
                v.forEach(chart->{
                    Map<String, String> map = ClassUtils.coverToMapJson(chart, null, null, false, 0);
                    map.put("dmtxt",orgMap.get(map.get("dmval")).getKey());
                    list.add(map);
                });
                versionMap.put(k, list);
            });
            dmCalcMap.put(caart, versionMap);
        });
        return dmCalcMap;
    }
}
