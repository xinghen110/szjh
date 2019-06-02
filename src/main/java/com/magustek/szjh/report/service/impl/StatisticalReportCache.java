package com.magustek.szjh.report.service.impl;

import com.google.common.base.Strings;
import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.basedataset.service.IEPlanSelectValueSetService;
import com.magustek.szjh.configset.bean.IEPlanBusinessItemSet;
import com.magustek.szjh.configset.bean.OrganizationSet;
import com.magustek.szjh.configset.service.IEPlanBusinessItemSetService;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.vo.RollPlanHeadDataArchiveVO;
import com.magustek.szjh.plan.service.PlanHeaderService;
import com.magustek.szjh.plan.service.RollPlanArchiveService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.constant.PlanheaderCons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StatisticalReportCache {
    private OrganizationSetService organizationSetService;
    private RollPlanArchiveService rollPlanArchiveService;
    private IEPlanSelectValueSetService iePlanSelectValueSetService;
    private IEPlanBusinessItemSetService iePlanBusinessItemSetService;
    private PlanHeaderService planHeaderService;

    public StatisticalReportCache(OrganizationSetService organizationSetService, RollPlanArchiveService rollPlanArchiveService, IEPlanSelectValueSetService iePlanSelectValueSetService, IEPlanBusinessItemSetService iePlanBusinessItemSetService, PlanHeaderService planHeaderService) {
        this.organizationSetService = organizationSetService;
        this.rollPlanArchiveService = rollPlanArchiveService;
        this.iePlanSelectValueSetService = iePlanSelectValueSetService;
        this.iePlanBusinessItemSetService = iePlanBusinessItemSetService;
        this.planHeaderService = planHeaderService;
    }

    @Cacheable(value = "ExecuteData")
    public List<Map<String, String>> getExecuteData(Long id, String version){
        PlanHeader planHeader = planHeaderService.getById(id);
        String jhval = planHeader.getJhval();
        Map<String, List<OrganizationSet>> dmartMap = organizationSetService.getOrgMapByDmart("D110");
        List<Map<String, String>> list = new ArrayList<>();
        //获取计划数据
        List<RollPlanHeadDataArchiveVO> rollPlanList = rollPlanArchiveService
                .getListByPlanHeaderId(id)
                .stream()
                .filter(vo->vo.getWears().compareTo(BigDecimal.ZERO) > 0)   //过滤掉金额不大于0的计划
                .filter(vo->!Strings.isNullOrEmpty(vo.getHtnum()))  //过滤掉合同管理编号为空的计划
                .collect(Collectors.toList());
        //获取待对比取数指标数据
        List<IEPlanSelectValueSet> selectValueList = iePlanSelectValueSetService.getAllByVersion(version);
        Map<String, List<IEPlanSelectValueSet>> htnumMap = selectValueList
                .stream()
                .collect(Collectors.groupingBy(IEPlanSelectValueSet::getHtnum));
        Map<String, List<IEPlanSelectValueSet>> htsnoMap = selectValueList
                .stream()
                .collect(Collectors.groupingBy(IEPlanSelectValueSet::getHtsno));
        //历史能力值列表
        List<IEPlanBusinessItemSet> caartList = iePlanBusinessItemSetService
                .getAll()
                .stream()
                .filter(i -> !Strings.isNullOrEmpty(i.getCaart()))
                .collect(Collectors.toList());
        //业务计算指标列表
        // key-imnum, value-caart
        Map<String, String> caartMap = new HashMap<>();
        caartList.forEach(c->
                caartMap.put(c.getImnum(), c.getCaart())
        );
        // key-imnum, value-sdart
        Map<String, String> sdartMap = new HashMap<>();
        caartList.forEach(c->
                sdartMap.put(c.getImnum(), c.getSdart())
        );
        //分历史能力值计算计划完成情况
        rollPlanList.forEach(head->
            head.getItemList().forEach(item->{
                //如果当前节点是取数节点-【G】,则不参与统计
                if("G".equals(item.getCtdtp())){
                    return;
                }
                //如果日期为空，则不参与统计
                if(Strings.isNullOrEmpty(item.getDtval())){
                    return;
                }
                //容错设计
                if(Strings.isNullOrEmpty(sdartMap.get(item.getImnum()))){
                    return;
                }
                //Map<String, List<IEPlanSelectValueSet>> htnumSdartMap;
                Map<String, List<IEPlanSelectValueSet>> htsnoSdartMap;
                //容错设计
                if(ClassUtils.isEmpty(htnumMap.get(head.getHtnum()))){
                    return;
                }

                try {
                    htsnoSdartMap = htsnoMap.get(head.getHtsno()).stream().collect(Collectors.groupingBy(IEPlanSelectValueSet::getSdart));
                    Map<String, String> map = new HashMap<>();

                    map.put("htsno", head.getHtsno());
                    map.put("htnum", head.getHtnum());
                    map.put("wears", head.getWears().toString());
                    //获取部门
                    List<String> dmartList = Arrays.stream(head.getDmval().split(",")).filter(d -> d.contains("D110:")).collect(Collectors.toList());
                    if(!ClassUtils.isEmpty(dmartList)){
                        map.put("dpnum", dmartList.get(0).replace("D110:",""));    //部门代码
                        map.put("dpnam", dmartMap.get(map.get("dpnum")).get(0).getDpnam());             //部门名称
                    }

                    map.put(PlanheaderCons.WEARS,head.getWears().toString());

                    map.put("id", item.getId().toString());
                    map.put("caart", caartMap.get(item.getImnum()));
                    map.put("imnum", item.getImnum());
                    map.put("G118", getSdval(htsnoSdartMap,"G118"));    //合同编号
                    map.put("G100", getSdval(htsnoSdartMap,"G100"));    //合同名称
                    map.put("G205", getSdval(htsnoSdartMap,"G205"));    //合同相对方
                    map.put("G203", getSdval(htsnoSdartMap,"G203"));    //合同承办人

                    map.put(PlanheaderCons.PLDAT, ClassUtils.StringToLocalDate(item.getDtval()).toString()); //计划日期
                    //实际发生日期
                    List<IEPlanSelectValueSet> sdartList = htsnoSdartMap.get(sdartMap.get(map.get("imnum")));
                    String actual = "0";
                    if(!ClassUtils.isEmpty(sdartList)){
                        actual = sdartList
                                .stream()
                                .map(IEPlanSelectValueSet::getSdval)
                                .max(Comparator.naturalOrder())
                                .orElse("0");
                    }
                    //如果该指标有计划日期以后的值，则认为已完成
                    if(Integer.parseInt(actual) < Integer.parseInt(planHeader.getCkdate().replaceAll("-",""))){
                        map.put(PlanheaderCons.CPDAT, "");
                        //是否延期
                        map.put(PlanheaderCons.DLFLG,"true");
                    }else{
                        map.put(PlanheaderCons.CPDAT, ClassUtils.StringToLocalDate(actual).toString());

                        //是否延期
                        map.put(PlanheaderCons.DLFLG,
                                String.valueOf(map.get(PlanheaderCons.CPDAT).compareTo(map.get(PlanheaderCons.PLDAT)) > 0));
                        //到目前为止延期天数
                        long delay = LocalDate.parse(map.get(PlanheaderCons.CPDAT)).toEpochDay()
                                - LocalDate.parse(map.get(PlanheaderCons.PLDAT)).toEpochDay();
                        map.put(PlanheaderCons.DELAY, String.valueOf(delay));
                    }
                    //计划外（非当月计划）已完成支付的合同
                    if(!Strings.isNullOrEmpty(map.get(PlanheaderCons.CPDAT))
                            && !map.get(PlanheaderCons.CPDAT).startsWith(jhval)){
                        map.put(PlanheaderCons.OUTER,"√");
                    }
                    list.add(map);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            })
        );
        return list;
    }
    private String getSdval(Map<String, List<IEPlanSelectValueSet>> sdartMap, String sdart){
        List<IEPlanSelectValueSet> list = sdartMap.get(sdart);
        if(ClassUtils.isEmpty(list)){
            return "";
        }
        return list.get(0).getSdval();
    }
}
