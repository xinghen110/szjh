package com.magustek.szjh.basedataset.service.impl;

import com.google.common.base.Strings;
import com.magustek.szjh.basedataset.dao.DmCalcStatisticsDAO;
import com.magustek.szjh.basedataset.entity.CalculateResult;
import com.magustek.szjh.basedataset.entity.DmCalcStatistics;
import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;
import com.magustek.szjh.basedataset.service.CalculateResultService;
import com.magustek.szjh.basedataset.service.DmCalcStatisticsService;
import com.magustek.szjh.basedataset.service.IEPlanDimenValueSetService;
import com.magustek.szjh.configset.bean.IEPlanCalculationSet;
import com.magustek.szjh.configset.bean.OrganizationSet;
import com.magustek.szjh.configset.service.IEPlanCalculationSetService;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("DmCalcStatisticsService")
public class DmCalcStatisticsServiceImpl implements DmCalcStatisticsService {
    private IEPlanDimenValueSetService iePlanDimenValueSetService;
    private CalculateResultService calculateResultService;
    private DmCalcStatisticsDAO dmCalcStatisticsDAO;
    private OrganizationSetService organizationSetService;
    private IEPlanCalculationSetService iePlanCalculationSetService;

    public DmCalcStatisticsServiceImpl(IEPlanDimenValueSetService iePlanDimenValueSetService, CalculateResultService calculateResultService, DmCalcStatisticsDAO dmCalcStatisticsDAO, OrganizationSetService organizationSetService, IEPlanCalculationSetService iePlanCalculationSetService) {
        this.iePlanDimenValueSetService = iePlanDimenValueSetService;
        this.calculateResultService = calculateResultService;
        this.dmCalcStatisticsDAO = dmCalcStatisticsDAO;
        this.organizationSetService = organizationSetService;
        this.iePlanCalculationSetService = iePlanCalculationSetService;
    }

    @Override
    public List<DmCalcStatistics> save(List<DmCalcStatistics> list) {
        dmCalcStatisticsDAO.save(list);
        return list;
    }

    @Transactional
    @Override
    public void deleteByVersion(String version) {
        version = ClassUtils.checkVersion(version);
        dmCalcStatisticsDAO.deleteAllByVersion(version);
    }

    @Transactional
    @Override
    public int statisticByVersion(String version) {
        version = ClassUtils.checkVersion(version);
        log.info("计算版本号："+version);
        List<DmCalcStatistics> statisticsList = new ArrayList<>();
        //根据版本获取维度数据，并根据dmart分组。
        Map<String, List<IEPlanDimenValueSet>> dmartMap = iePlanDimenValueSetService
                .getAllByVersion(version)
                .stream()
                .collect(Collectors.groupingBy(IEPlanDimenValueSet::getDmart));
        log.info("维度指标数量："+dmartMap.size());
        //根据版本获取计算数据，并根据htsno分组。
        Map<String, List<CalculateResult>> calcMap = calculateResultService
                .getAllByVersion(version)
                .stream()
                .collect(Collectors.groupingBy(CalculateResult::getHtsno));
        log.info("计算合同数量："+calcMap.size());
        if(ClassUtils.isEmpty(dmartMap)){//非空判断
            return 0;
        }
        //维度指标分组
        dmartMap.forEach((dmartKey, dmartValue)->{
            Map<String, List<IEPlanDimenValueSet>> dmvalMap = dmartValue
                    .stream()
                    .collect(Collectors.groupingBy(IEPlanDimenValueSet::getDmval));
            if(ClassUtils.isEmpty(dmvalMap)){//非空判断
                return;
            }
            //维度值分组
            dmvalMap.forEach((dmvalKey, dmvalValue)->{
                if(ClassUtils.isEmpty(dmvalValue)){//非空判断
                    return;
                }
                Map<String, List<IEPlanDimenValueSet>> htsnoMap = dmvalValue
                        .stream()
                        .collect(Collectors.groupingBy(IEPlanDimenValueSet::getHtsno));

                List<DmCalcStatistics> htsnoList = new ArrayList<>();
                //htsno分组
                htsnoMap.forEach((htsnoKey, htsnoValue)->{
                    List<CalculateResult> calcByHtsno = calcMap.get(htsnoKey);
                    if(ClassUtils.isEmpty(calcByHtsno)){
                        return;
                    }
                    Map<String, List<CalculateResult>> caartMap = calcByHtsno
                            .stream()
                            .collect(Collectors.groupingBy(CalculateResult::getCaart));
                    //计算指标分组
                    caartMap.forEach((caartKey, caartValue)->{
                        if(ClassUtils.isEmpty(caartValue)){//非空判断
                            return;
                        }
/*                        Optional<CalculateResult> sum = caartValue.stream().reduce((a, b)->{
                            BigDecimal sum1 = new BigDecimal(a.getCaval());
                            BigDecimal sum2 = new BigDecimal(b.getCaval());
                            a.setCaval(sum1.add(sum2).toString());
                            return a;
                        });*/

                        List<BigDecimal> sum = new ArrayList<>(1);
                        sum.add(new BigDecimal(0));

                        caartValue.forEach(c-> sum.set(0,sum.get(0).add(new BigDecimal(c.getCaval()))));


                        int count = caartValue.size();
                        DmCalcStatistics statistics = new DmCalcStatistics();
                        statistics.setDmart(dmartKey);
                        statistics.setDmval(dmvalKey);
                        statistics.setCaart(caartKey);
                        statistics.setCaval(sum.get(0).toString());
                        statistics.setJswdqz(count);
                        statistics.setVersion(caartValue.get(0).getVersion());
                        htsnoList.add(statistics);
                    });
                });
                //由于先按照htsno分组，后按照caart分组，需要再根据htsno加和一遍
                Map<String, List<DmCalcStatistics>> caartMapForHtsno = htsnoList
                        .stream()
                        .collect(Collectors.groupingBy(DmCalcStatistics::getCaart));
                if(ClassUtils.isEmpty(caartMapForHtsno)){//非空判断
                    return;
                }
                caartMapForHtsno.forEach((caartMapForHtsnoKey, caartMapForHtsnoValue)->{
                    if(!ClassUtils.isEmpty(caartMapForHtsnoValue)){
                        DmCalcStatistics s = caartMapForHtsnoValue.get(0);
                        for(DmCalcStatistics dm : caartMapForHtsnoValue){
                            BigDecimal sum1 = new BigDecimal(s.getCaval());
                            BigDecimal sum2 = new BigDecimal(dm.getCaval());
                            //值加和
                            s.setCaval(sum1.add(sum2).toString());
                            //笔数加和
                            s.setJswdqz(s.getJswdqz()+dm.getJswdqz());
                        }
                        BigDecimal sum1 = new BigDecimal(s.getCaval());
                        BigDecimal sum2 = new BigDecimal(s.getJswdqz());
                        //计算历史能力值
                        s.setHisval(sum1.divide(sum2,0,BigDecimal.ROUND_DOWN).intValue());
                        statisticsList.add(s);
                    }
                });
            });
        });
        //清除原有版本
        dmCalcStatisticsDAO.deleteAllByVersion(version);
        //保存新版本
        dmCalcStatisticsDAO.save(statisticsList);
        return statisticsList.size();
    }

    @Override
    public List<Map<String, Object>> getStatisticsByDmartAndVersion(String dmart, String version) {
        Map<String, List<DmCalcStatistics>> statisticsMap = dmCalcStatisticsDAO
                .findAllByDmartAndVersion(dmart, version)
                .stream()
                .collect(Collectors.groupingBy(DmCalcStatistics::getDmval));
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, List<OrganizationSet>> orgMap = organizationSetService.getOrgMapByDmart(dmart);
        if(orgMap==null){
            return list;
        }

        statisticsMap.forEach((dmval, statisticsList)->{
            Map<String, Object> map = new HashMap<>();
            list.add(map);
            map.put("dmval",dmval);
            map.put("dmart",dmart);
            //历史能力值k-v
            statisticsList.forEach(statistics-> map.put(statistics.getCaart(), statistics.getHisval().toString()));
            //填充组织信息
            organizationSetService.fillMap(orgMap, map, dmart, dmval);
        });
        return list.stream().sorted(Comparator.comparingInt(m -> Integer.parseInt((String)m.get("sort")))).collect(Collectors.toList());
    }

    @Override
    public String getCaval(String version, String dmart, String dmval, String caart, Map<String, String> cache) {
        //容错
        if(Strings.isNullOrEmpty(caart)){
            return "";
        }
        //缓存命中
        String key = version+"-"+dmart+"-"+dmval+"-"+caart;
        if(cache.containsKey(key)){
            return cache.get(key);
        }

        DmCalcStatistics dm = dmCalcStatisticsDAO.findFirstByVersionAndDmartAndDmvalAndCaart(
                version,
                dmart,
                dmval,
                caart);

        if(dm != null){
            //加入缓存
            cache.put(key, dm.getHisval()+"");
            return cache.get(key);
        }else{
            //加入缓存
            cache.put(key, "");
            return "";
        }
    }

    @Override
    public List<DmCalcStatistics> getStatisticsByDmartAndCaartAndVersion(String dmart, String caart, String version) {
        return dmCalcStatisticsDAO.findAllByDmartAndCaartAndVersion(dmart, caart, version);
    }

    @Override
    public List<DmCalcStatistics> getDmCalcChart(String[] versionList, String[] dmvalList, String caart) {
        List<String> caartList = new ArrayList<>();
        if(Strings.isNullOrEmpty(caart)){
            caartList = iePlanCalculationSetService.getAll().stream().map(IEPlanCalculationSet::getCaart).collect(Collectors.toList());
        }else{
            caartList.add(caart);
        }
        return dmCalcStatisticsDAO.findAllByVersionInAndDmvalInAndAndCaartInOrderByVersion(versionList, dmvalList, caartList.toArray(new String[0]));
    }
}
