package com.magustek.szjh.statistic.service.impl;

import com.google.common.base.Strings;
import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.basedataset.service.IEPlanSelectValueSetService;
import com.magustek.szjh.configset.bean.IEPlanScreenItemSet;
import com.magustek.szjh.configset.service.IEPlanScreenService;
import com.magustek.szjh.statistic.bean.Report;
import com.magustek.szjh.statistic.service.StatisticalReportService;
import com.magustek.szjh.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("StatisticalReportService")
public class StatisticalReportServiceImpl implements StatisticalReportService {

    private IEPlanScreenService iePlanScreenService;
    private IEPlanSelectValueSetService iePlanSelectValueSetService;

    public StatisticalReportServiceImpl(IEPlanScreenService iePlanScreenService, IEPlanSelectValueSetService iePlanSelectValueSetService) {
        this.iePlanScreenService = iePlanScreenService;
        this.iePlanSelectValueSetService = iePlanSelectValueSetService;
    }

    @Override
    public Page<Map<String, String>> getOutputTaxDetailByVersion(Report report) throws Exception{
        List<Map<String, String>> detailList = new ArrayList<>();
        List<IEPlanSelectValueSet> selectValueSetList;
        //获取待使用取数指标集合
        String outputTax = "statisticalReport/getOutputTaxDetailByVersion";
        List<IEPlanScreenItemSet> itemListByIntfa = iePlanScreenService.getItemListByIntfa(outputTax);
        if(ClassUtils.isEmpty(itemListByIntfa)){
            return new PageImpl<>(new ArrayList<>());
        }
        //获取【检索项标识】字段
        List<IEPlanScreenItemSet> serchList = itemListByIntfa.stream().filter(i -> "X".equals(i.getSerch())).collect(Collectors.toList());
        List<String> sdvarList = itemListByIntfa.stream().map(IEPlanScreenItemSet::getSdvar).collect(Collectors.toList());
        //根据serch过滤
        if(!ClassUtils.isEmpty(serchList)){
            //根据取数指标取出数据
            selectValueSetList = iePlanSelectValueSetService.getAllByVersionAndSdvarIn(report.getVersion(), serchList.get(0).getSdvar(), sdvarList);
        }else{
            throw new Exception("屏幕配置出错，无【检索项标识】！");
        }



        if(ClassUtils.isEmpty(selectValueSetList)){
            return new PageImpl<>(new ArrayList<>());
        }

        Map<String, List<IEPlanSelectValueSet>> taxList = selectValueSetList.stream().collect(Collectors.groupingBy(IEPlanSelectValueSet::getHtnum));

        taxList.forEach((key,value)->{
            Map<String, String> map = new HashMap<>(2);
            value.forEach(v->
                map.put(v.getSdart(), v.getSdval())
            );
            detailList.add(map);
        });

        int start = report.getPageRequest().getOffset();
        int pageSize = report.getPageRequest().getPageSize();
        return new PageImpl<>(detailList.subList(start, start+pageSize), report.getPageRequest(), detailList.size());
    }
}
