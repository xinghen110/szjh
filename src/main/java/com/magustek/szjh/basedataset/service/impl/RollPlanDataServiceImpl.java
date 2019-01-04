package com.magustek.szjh.basedataset.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.magustek.szjh.Holiday.service.HolidayService;
import com.magustek.szjh.basedataset.dao.RollPlanDataDAO;
import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import com.magustek.szjh.basedataset.entity.RollPlanItemData;
import com.magustek.szjh.basedataset.entity.helper.RollPlanDataHelper;
import com.magustek.szjh.basedataset.service.DmCalcStatisticsService;
import com.magustek.szjh.basedataset.service.IEPlanSelectValueSetService;
import com.magustek.szjh.basedataset.service.RollPlanDataService;
import com.magustek.szjh.configset.bean.IEPlanBusinessHeadSet;
import com.magustek.szjh.configset.bean.IEPlanBusinessItemSet;
import com.magustek.szjh.configset.bean.IEPlanReportHeadSet;
import com.magustek.szjh.configset.bean.vo.IEPlanBusinessHeadSetVO;
import com.magustek.szjh.configset.bean.vo.IEPlanBusinessItemSetVO;
import com.magustek.szjh.configset.service.IEPlanBusinessHeadSetService;
import com.magustek.szjh.configset.service.IEPlanBusinessItemSetService;
import com.magustek.szjh.configset.service.IEPlanReportHeadSetService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.constant.IEPlanSelectDataConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("RollPlanDataService")
public class RollPlanDataServiceImpl implements RollPlanDataService {
    private RollPlanDataDAO rollPlanDataDAO;
    private IEPlanBusinessHeadSetService iePlanBusinessHeadSetService;
    private IEPlanBusinessItemSetService iePlanBusinessItemSetService;
    private IEPlanReportHeadSetService iePlanReportHeadSetService;
    private IEPlanSelectValueSetService iePlanSelectValueSetService;
    private DmCalcStatisticsService dmCalcStatisticsService;
    private HolidayService holidayService;

    private List<IEPlanBusinessHeadSetVO> headSetList;//滚动计划抬头列表
    private Map<String, List<IEPlanBusinessItemSetVO>> businessItemMap;//滚动计划明细列表（以抬头编号分组）

    private String version;//全局变量

    public RollPlanDataServiceImpl(RollPlanDataDAO rollPlanDataDAO, IEPlanBusinessHeadSetService iePlanBusinessHeadSetService, IEPlanBusinessItemSetService iePlanBusinessItemSetService, IEPlanReportHeadSetService iePlanReportHeadSetService, IEPlanSelectValueSetService iePlanSelectValueSetService, DmCalcStatisticsService dmCalcStatisticsService, HolidayService holidayService) {
        this.rollPlanDataDAO = rollPlanDataDAO;
        this.iePlanBusinessHeadSetService = iePlanBusinessHeadSetService;
        this.iePlanBusinessItemSetService = iePlanBusinessItemSetService;
        this.iePlanReportHeadSetService = iePlanReportHeadSetService;
        this.iePlanSelectValueSetService = iePlanSelectValueSetService;
        this.dmCalcStatisticsService = dmCalcStatisticsService;
        this.holidayService = holidayService;
        this.init();
    }
    //初始化数据
    private void init(){
        headSetList = new ArrayList<>();
        //获取所有报表配置
        List<IEPlanReportHeadSet> reportHeadSetList = iePlanReportHeadSetService.getAll();
        reportHeadSetList.forEach(header->{
            //遍历所有报表配置，获取滚动计划抬头列表
            headSetList.addAll(iePlanBusinessHeadSetService.getAllVOByBukrsAndRptyp(header.getBukrs(), header.getRptyp()));
        });
        //滚动计划明细列表（以抬头编号分组）
        businessItemMap = iePlanBusinessItemSetService.getAllVO().stream().collect(Collectors.groupingBy(IEPlanBusinessItemSet::getHdnum));
    }

    @Override
    public List<RollPlanHeadData> save(List<RollPlanHeadData> list) {
        rollPlanDataDAO.save(list);
        return list;
    }

    @Override
    public List<RollPlanHeadData> getAllByVersion(String version) {
        return rollPlanDataDAO.findAllByVersion(version);
    }

    @Override
    public void deleteAllByVersion(String version) {
        rollPlanDataDAO.deleteAllByVersion(version);
    }

    @Override
    public List<RollPlanHeadData> calculateByVersion(String version) {
        this.version = version;
        List<RollPlanHeadData> rollPlanHeadDataList = new ArrayList<>();
        List<RollPlanItemData> rollPlanItemData = new ArrayList<>();

        /* 取数指标集合（用于从IEPlanSelectValueSet表取基础数据）：
                从【业务抬头配置】取出所待使用的【取数指标集合】 */
        Set<String> sdartSet = new HashSet<>();//使用set去重
        headSetList.stream()
                .map(IEPlanBusinessHeadSet::getVariv)
                .forEach(v-> sdartSet.addAll(Lists.newArrayList(v.split(","))));


        /* 基础数据（根据htsno分组）：
                【业务取数指标】表中选取【PFLAG】="X" and sdart in 【取数指标集合】的【合同数据】数据 */
        List<IEPlanSelectValueSet> basicValueList = iePlanSelectValueSetService
                .getAllByVersionAndSdartListAndPflag(this.version, sdartSet, "X");
        //根据htsno分组
        Map<String, List<IEPlanSelectValueSet>> valueListByHtsno = basicValueList.stream()
                .collect(Collectors.groupingBy(IEPlanSelectValueSet::getHtsno));
        //根据htnum分组
        Map<String, List<IEPlanSelectValueSet>> valueListByHtnum = basicValueList.stream()
                .collect(Collectors.groupingBy(IEPlanSelectValueSet::getHtnum));


        /* 计划抬头列表（根据htsno分组）：
                遍历【合同数据】，根据【业务抬头配置】里的【显示条件】公式进行计算，得出【计算结果】(boolean) */
        Map<String, List<IEPlanBusinessHeadSetVO>> htsnoHeadList = new HashMap<>(valueListByHtsno.size());
        valueListByHtsno.forEach((htsno, valueList)->{
            List<IEPlanBusinessHeadSetVO> headList = new ArrayList<>();
            headSetList.forEach(h->{
                if(h.calc(valueList)){
                    h.setItemVOList(businessItemMap.get(h.getHdnum()));
                    headList.add(h);
                }
            });
            htsnoHeadList.put(htsno, headList);
        });

        /* 计算滚动计划明细单 */
        htsnoHeadList.forEach((htsno, headSetList)->{

            List<RollPlanDataHelper> rollPlanDataHelperList = new ArrayList<>();//同一个合同的计划列表
            Map<String, List<IEPlanSelectValueSet>> sdartValueMap = valueListByHtsno
                    .get(htsno)
                    .stream()
                    .collect(Collectors.groupingBy(IEPlanSelectValueSet::getSdart));
            /* 根据明细配置，计算明细单 */
            headSetList.forEach(head->{
                //获取明细配置列表
                List<IEPlanBusinessItemSetVO> itemVOList = head.getItemVOList();

                //获取取数方式列表
                Map<String, List<IEPlanBusinessItemSetVO>> groupBySdtyp = itemVOList
                        .stream()
                        .collect(Collectors.groupingBy(IEPlanBusinessItemSetVO::getSdtyp));
                //处理类型为【G】的指标
                GetPlanData(groupBySdtyp,
                        htsno,
                        head,
                        sdartValueMap,
                        rollPlanDataHelperList);
                //处理类型为【C】的指标

                //获取计算值的项目
                List<IEPlanBusinessItemSetVO> getItem = groupBySdtyp.get(IEPlanBusinessItemSet.CALC);
                //计算起始节点
                IEPlanBusinessItemSetVO startItem = null;
                //获取开始、结束环节
                for(IEPlanBusinessItemSetVO i : getItem){
                    if("X".equals(i.getHjbgn())){
                        startItem = i;
                    }
                }

                //根据项目编号将项目分组
                Map<String, List<IEPlanBusinessItemSetVO>> ItemMap = getItem
                        .stream()
                        .collect(Collectors.groupingBy(IEPlanBusinessItemSetVO::getImnum));

                ArrayList<IEPlanBusinessItemSetVO> calcList = new ArrayList<>();
                while (startItem != null){
                    calcList.add(startItem);
                    //下一环节编号
                    String nimnu = startItem.getNimnu();
                    //获取下一环节
                    if(Strings.isNullOrEmpty(nimnu)){
                        startItem = ItemMap.get(nimnu).get(0);
                    }else{
                        startItem = null;
                    }

                }
                //TODO 将该流水号下所有值按照【对应金额指标】分组求和，第次相减后，如果差额小于等于0，记录上一组最新日期（根据取值指标）；如果大于0，创建一个【滚动计划明细单】。
                //遍历待计算节点列表
                for(IEPlanBusinessItemSetVO item : calcList){

                    String maxDate = "";//当前节点日期最大值
                    String maxHtnum = "";//当前节点日期最大值对应的htnum
                    double sumCurr = 0d;//当前节点金额总和
                    List<IEPlanSelectValueSet> sdcurList = sdartValueMap.get(item.getSdcur());
                    if(!ClassUtils.isEmpty(sdcurList)){
                        sumCurr = sdcurList.stream().mapToDouble(v->{
                            try{
                                return Double.parseDouble(v.getSdval());
                            }catch (NumberFormatException e){
                                return 0d;
                            }
                        }).sum();
                    }

                    //取得最新的日期及其对应的htnum
                    Optional<IEPlanSelectValueSet> first = sdartValueMap.get(item.getSdart()).stream().max(Comparator.comparing(IEPlanSelectValueSet::getSdval));
                    if(first.isPresent()){
                        maxDate = first.get().getSdval();
                        maxHtnum = first.get().getHtnum();
                    }
                    //遍历help列表，处理所有滚动计划条目
                    for(RollPlanDataHelper helper : rollPlanDataHelperList) {
                        List<RollPlanItemData> localItemList = helper.getItemList();
                        RollPlanHeadData headData = helper.getHeadData();

                        RollPlanItemData itemData = new RollPlanItemData();
                        itemData.setHeadId(helper.getHeadData());
                        itemData.setImnum(item.getImnum());
                        localItemList.add(itemData);
                        //如果节点列表是空，或是开始计算环节，说明是第一个节点，需要手动增加环节
                        if (ClassUtils.isEmpty(localItemList) || "X".equals(item.getHjbgn())) {

                            itemData.setDtval(maxDate);
                            itemData.setCtdtp(IEPlanBusinessItemSet.GET);

                            headData.setWears(BigDecimal.valueOf(sumCurr));
                            headData.setHtnum(maxHtnum);
                            continue;
                        }

                        //获取上一个环节
                        RollPlanItemData lastItem = localItemList.get(localItemList.size() - 1);
                        //计算节点差值
                        BigDecimal subCurr = headData.getWears().subtract(BigDecimal.valueOf(sumCurr));

                        if (subCurr.compareTo(BigDecimal.ZERO) <= 0){
                            // 金额无差值
                            if(IEPlanBusinessItemSet.GET.equals(lastItem.getCtdtp())){
                                //上一节点是取值节点，无差值的当前节点必定也是取值节点
                                itemData.setDtval(maxDate);
                                itemData.setCtdtp(IEPlanBusinessItemSet.GET);
                                headData.setHtnum(maxHtnum);
                            }else{
                                //上一节点是计算节点，本节点必定也是计算节点
                                String calcDate;
                                try {
                                    //根据上一节点日期，以及当前节点的能力值，计算计划日期（考虑工作日）
                                    calcDate = holidayService.getWordDay(
                                            LocalDate.parse(lastItem.getDtval()),
                                            itemData.getCaval(),
                                            true).toString().replace("-","");
                                } catch (Exception e) {
                                    log.error(e.getMessage());
                                    e.printStackTrace();
                                    calcDate = "";
                                }

                                itemData.setCtdtp(IEPlanBusinessItemSet.CALC);
                                itemData.setCaval(this.getDmval(item.getCaart()));
                                itemData.setDtval(calcDate);
                            }
                        }else{
                            //金额有差值

                        }
                    }

                }
            });
        });

        return rollPlanHeadDataList;
    }

    //处理类型为【G】的指标
    private void GetPlanData(Map<String, List<IEPlanBusinessItemSetVO>> groupBySdtyp,
                             String htsno,
                             IEPlanBusinessHeadSetVO head,
                             Map<String, List<IEPlanSelectValueSet>> sdartValueMap,
                             List<RollPlanDataHelper> rollPlanDataHelperList){
        RollPlanDataHelper helper = new RollPlanDataHelper();
        List<RollPlanItemData> itemDataList = new ArrayList<>();
        helper.setItemList(itemDataList);
        rollPlanDataHelperList.add(helper);
        //获取待取值的项目
        List<IEPlanBusinessItemSetVO> getItem = groupBySdtyp.get(IEPlanBusinessItemSet.GET);

        //每一个明细配置会产生一个滚动计划单
        RollPlanHeadData plan = new RollPlanHeadData();
        plan.setHtsno(htsno);
        plan.setHdnum(head.getHdnum());
        plan.setVersion(version);
        helper.setHeadData(plan);
        //获取类型为G的值
        getItem.forEach(c->{
            //当前项目的取值指标
            String sdart = c.getSdart();
            String sdtyp = c.getSdtyp();
            List<IEPlanSelectValueSet> sdartValue = sdartValueMap.get(sdart);
            if(ClassUtils.isEmpty(sdartValue)){
                OptionalInt maxInt;
                RollPlanItemData itemData = new RollPlanItemData();
                itemData.setImnum(c.getImnum());
                itemData.setCtdtp("G");
                switch (sdtyp){
                    //日期
                    case IEPlanSelectDataConstant.RESULT_TYPE_DATS:
                        //取最大值
                        maxInt = sdartValue.stream().mapToInt(m -> Integer.valueOf(m.getSdval())).max();
                        if(maxInt.isPresent()){
                            itemData.setDtval(String.valueOf(maxInt.getAsInt()));
                        }else {
                            itemData.setDtval("");
                        }
                        //加入明细表
                        itemData.setHeadId(plan);
                        itemDataList.add(itemData);
                        break;
                    //字符串
                    case IEPlanSelectDataConstant.RESULT_TYPE_CHAR:
                        StringBuffer sb = new StringBuffer();
                        sdartValue.forEach(v-> sb.append(v.getSdval()).append("<br>"));
                        //合同条款
                        itemData.setStval(sb.toString());
                        //加入明细表
                        itemData.setHeadId(plan);
                        itemDataList.add(itemData);
                        break;
                    //金额
                    case IEPlanSelectDataConstant.RESULT_TYPE_CURR:
                        IEPlanSelectValueSet v = sdartValue.get(0);
                        if(v!=null){
                            plan.setWears(new BigDecimal(v.getSdval()));
                            plan.setHtnum(v.getHtnum());
                        }else {
                            plan.setWears(new BigDecimal(0));
                        }
                        break;
                }
            }
        });
    }

    //根据版本，维度代码获取【计划能力值】
    private int getDmval(String caart){
        try {
            String dmval = ContextUtils.getCompany().getOrgcode();
            String caval = dmCalcStatisticsService.getCaval(this.version, "D100", dmval, caart);
            //四舍五入取整
            BigDecimal decimal = new BigDecimal(caval);
            decimal = decimal.setScale(0,BigDecimal.ROUND_HALF_UP);
            return decimal.intValue();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
