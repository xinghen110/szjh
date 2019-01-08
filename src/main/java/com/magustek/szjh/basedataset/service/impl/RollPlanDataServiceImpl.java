package com.magustek.szjh.basedataset.service.impl;

import com.google.common.base.Strings;
import com.magustek.szjh.Holiday.service.HolidayService;
import com.magustek.szjh.basedataset.dao.RollPlanDataDAO;
import com.magustek.szjh.basedataset.dao.RollPlanHeadDataDAO;
import com.magustek.szjh.basedataset.dao.RollPlanItemDataDAO;
import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import com.magustek.szjh.basedataset.entity.RollPlanItemData;
import com.magustek.szjh.basedataset.entity.helper.RollPlanDataHelper;
import com.magustek.szjh.basedataset.service.DmCalcStatisticsService;
import com.magustek.szjh.basedataset.service.IEPlanSelectValueSetService;
import com.magustek.szjh.basedataset.service.RollPlanDataService;
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
    private RollPlanHeadDataDAO rollPlanHeadDataDAO;
    private RollPlanItemDataDAO rollPlanItemDataDAO;


    private IEPlanBusinessHeadSetService iePlanBusinessHeadSetService;
    private IEPlanBusinessItemSetService iePlanBusinessItemSetService;
    private IEPlanReportHeadSetService iePlanReportHeadSetService;
    private IEPlanSelectValueSetService iePlanSelectValueSetService;
    private DmCalcStatisticsService dmCalcStatisticsService;
    private HolidayService holidayService;

    private List<IEPlanBusinessHeadSetVO> headSetList;//滚动计划抬头列表
    private Map<String, List<IEPlanBusinessItemSetVO>> businessItemMap;//滚动计划明细列表（以抬头编号分组）

    private String version;//全局变量

    public RollPlanDataServiceImpl(RollPlanDataDAO rollPlanDataDAO, RollPlanHeadDataDAO rollPlanHeadDataDAO, RollPlanItemDataDAO rollPlanItemDataDAO, IEPlanBusinessHeadSetService iePlanBusinessHeadSetService, IEPlanBusinessItemSetService iePlanBusinessItemSetService, IEPlanReportHeadSetService iePlanReportHeadSetService, IEPlanSelectValueSetService iePlanSelectValueSetService, DmCalcStatisticsService dmCalcStatisticsService, HolidayService holidayService) {
        this.rollPlanDataDAO = rollPlanDataDAO;
        this.rollPlanHeadDataDAO = rollPlanHeadDataDAO;
        this.rollPlanItemDataDAO = rollPlanItemDataDAO;
        this.iePlanBusinessHeadSetService = iePlanBusinessHeadSetService;
        this.iePlanBusinessItemSetService = iePlanBusinessItemSetService;
        this.iePlanReportHeadSetService = iePlanReportHeadSetService;
        this.iePlanSelectValueSetService = iePlanSelectValueSetService;
        this.dmCalcStatisticsService = dmCalcStatisticsService;
        this.holidayService = holidayService;
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
        this.init();

        List<RollPlanHeadData> rollPlanHeadDataList = new ArrayList<>();
        List<RollPlanItemData> rollPlanItemDataList = new ArrayList<>();


        /* 基础数据（根据htsno分组）：
                【业务取数指标】表中选取【PFLAG】="X" and sdart in 【取数指标集合】的【合同数据】数据 */
        List<IEPlanSelectValueSet> basicValueList = iePlanSelectValueSetService
                .getAllByVersionAndPflag(this.version, "X");
        //根据htsno分组
        Map<String, List<IEPlanSelectValueSet>> valueListByHtsno = basicValueList.stream()
                .collect(Collectors.groupingBy(IEPlanSelectValueSet::getHtsno));


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
                if(ClassUtils.isEmpty(getItem)){
                    return;
                }
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
                /*将该流水号下所有值按照【对应金额指标】分组求和，
                第次相减后，如果差额小于等于0，记录上一组最新日期（根据取值指标）；
                如果大于0，创建一个【滚动计划明细单】。*/
                //遍历待计算节点列表
                calcList.forEach(item->{
                    String maxDate = "";//当前节点日期最大值
                    String maxHtnum = "";//当前节点日期最大值对应的htnum
                    double sumCurr = 0d;//当前节点金额总和
                    List<IEPlanSelectValueSet> sdcurList = sdartValueMap.get(item.getSdcur());
                    if (!ClassUtils.isEmpty(sdcurList)) {
                        sumCurr = sdcurList.stream().mapToDouble(v -> {
                            try {
                                return Double.parseDouble(v.getSdval());
                            } catch (NumberFormatException e) {
                                return 0d;
                            }
                        }).sum();
                    }

                    //取得最新的日期及其对应的htnum
                    Optional<IEPlanSelectValueSet> first = sdartValueMap.get(item.getSdart()).stream().max(Comparator.comparing(IEPlanSelectValueSet::getSdval));
                    if (first.isPresent()) {
                        maxDate = first.get().getSdval();
                        maxHtnum = first.get().getHtnum();
                    }
                    item.setSdartValue(maxDate);//最新取数日期（如果取不到则为空）
                    item.setCaartValue(getDmval(item.getCaart()));//历史能力值
                    item.setSdcutValue(BigDecimal.valueOf(sumCurr));//累计金额
                    item.setMaxHtnum(maxHtnum);//最新日期对应的合同管理编号
                });

                //上一个环节
                IEPlanBusinessItemSetVO lastItemVO = null;
                for(IEPlanBusinessItemSetVO itemVO : calcList) {
                    //遍历help列表，处理所有滚动计划条目
                    RollPlanDataHelper helper;
                    int i = 0;

                    while(true){
                        //取待处理计划（因为列表一直在增加，只能使用程序判断循环。）
                        if(i<=rollPlanDataHelperList.size()){
                            i++;
                            helper = rollPlanDataHelperList.get(i);
                        }else{
                            break;
                        }

                        //上一节点金额
                        BigDecimal lastSdcutValue;
                        if(lastItemVO == null){
                            //如果无上一环节，则无差值（第一个环节）
                            lastSdcutValue = itemVO.getSdcutValue();
                        }else{
                            lastSdcutValue = lastItemVO.getSdcutValue();
                        }
                        //计算节点差值
                        BigDecimal subCurr = lastSdcutValue.subtract(itemVO.getSdcutValue());

                        if (subCurr.compareTo(BigDecimal.ZERO) > 0){
                            //金额有差值
                            //增加一条新的滚动计划
                            RollPlanDataHelper newHelper = new RollPlanDataHelper();
                            this.createHelper(newHelper, itemVO, calcList);
                            newHelper.getHeadData().setWears(subCurr);
                            newHelper.getHeadData().setHtsno(htsno);
                            newHelper.getHeadData().setHdnum(itemVO.getHdnum());
                            newHelper.getHeadData().setVersion(version);
                            rollPlanDataHelperList.add(newHelper);
                        }
                        //已存在计划节点计算
                        noDiff(helper,itemVO);
                        //设置上一个环节
                        lastItemVO = itemVO;
                    }
                }

                //根据合同约定条款，调整计划日期。
                rollPlanDataHelperList.forEach(helper->{
                    try {
                        calcContractStipulate(calcList, helper, sdartValueMap);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });

            });
            rollPlanHeadDataList.addAll(
                    rollPlanDataHelperList
                            .stream()
                            .map(RollPlanDataHelper::getHeadData)
                            .collect(Collectors.toList()));
            rollPlanDataHelperList
                    .stream()
                    .map(RollPlanDataHelper::getItemList)
                    .collect(Collectors.toList()).forEach(rollPlanItemDataList::addAll);
        });

        rollPlanHeadDataDAO.save(rollPlanHeadDataList);
        rollPlanItemDataDAO.save(rollPlanItemDataList);
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
    //滚动计划条目 - 金额无差异
    private void noDiff(RollPlanDataHelper helper,
                        IEPlanBusinessItemSetVO itemVO){
        //新建一个节点
        List<RollPlanItemData> itemList = helper.getItemList();
        RollPlanItemData itemData = new RollPlanItemData();
        itemList.add(itemData);

        //待计算项目：有且只有一个上一节点-【约定条款】
        if(itemList.size()<=1){
            return;
        }
        //上一节点
        RollPlanItemData lastItem = itemList.get(itemList.size()-2);

        //已存在的滚动计划，本节点必定是计算节点
        String calcDate;
        try {
            //TODO 工作日，待所有节点计算完毕后，统一计算
            //根据上一节点日期，以及当前节点的能力值，计算计划日期（考虑工作日）
/*            calcDate = holidayService.getWorkDay(
                    LocalDate.parse(lastItem.getDtval()),
                    itemVO.getCaartValue(),
                    true).toString().replace("-","");*/
            //根据上一节点日期，以及当前节点的能力值，计算计划日期
            calcDate = LocalDate.parse(lastItem.getDtval())
                    .plusDays(itemVO.getCaartValue())
                    .toString().replace("-","");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            calcDate = "";
        }

        itemData.setHeadId(helper.getHeadData());
        itemData.setImnum(itemVO.getImnum());
        itemData.setCtdtp(IEPlanBusinessItemSet.CALC);
        itemData.setCaval(itemVO.getCaartValue());
        itemData.setDtval(calcDate);
    }

    //滚动计划条目 - 创建新计划
    private void createHelper(RollPlanDataHelper newHelper,
                              IEPlanBusinessItemSetVO item,
                              List<IEPlanBusinessItemSetVO> calcList){
        //已发生环节列表（不包括当前环节）
        List<IEPlanBusinessItemSetVO> itemList = new ArrayList<>();
        for(IEPlanBusinessItemSetVO i : calcList){
            if(i.getImnum().equals(item.getImnum())){
                break;
            }
            itemList.add(i);
        }


        //新计划-抬头
        RollPlanHeadData newHeadData = new RollPlanHeadData();
        List<RollPlanItemData> newItemList = new ArrayList<>(itemList.size());
        newHelper.setHeadData(newHeadData);
        newHelper.setItemList(newItemList);

        //新计划-环节
        itemList.forEach(i->{
            newHeadData.setHtnum(i.getMaxHtnum());
            RollPlanItemData newItem = new RollPlanItemData();
            newItem.setHeadId(newHeadData);
            newItemList.add(newItem);

            newItem.setImnum(i.getImnum());
            newItem.setDtval(i.getSdartValue());
            newItem.setCtdtp(IEPlanBusinessItemSet.GET);
        });
    }

    //获取合同约定相关节点数据（待调整日期的节点列表）
    private List<IEPlanBusinessItemSetVO> getStipulateItemList(List<IEPlanBusinessItemSetVO> calcList,                                  Map<String, List<IEPlanSelectValueSet>> sdartValueMap){
        List<IEPlanBusinessItemSetVO> itemList = new ArrayList<>();
        //取出【合同约定起始环节】不为空的环节
        Optional<IEPlanBusinessItemSetVO> lastItemOP = calcList.stream().filter(i -> !Strings.isNullOrEmpty(i.getCtbgn())).findFirst();
        //根据环节编号分组
        Map<String, List<IEPlanBusinessItemSetVO>> itemMapByImnum = calcList.stream().collect(Collectors.groupingBy(IEPlanBusinessItemSetVO::getImnum));

        IEPlanBusinessItemSetVO beginItem;//约定起始环节
        IEPlanBusinessItemSetVO lastItem;//约定结束环节
        //如果没有合同约定结束环节，则返回空
        if(lastItemOP.isPresent()){
            lastItem = lastItemOP.get();
        }else{
            return null;
        }
        String ctbgn = lastItem.getCtbgn();
        List<IEPlanBusinessItemSetVO> tempList = itemMapByImnum.get(ctbgn);

        //如果没有合同约定起始环节，则返回空
        if(!ClassUtils.isEmpty(tempList)){
            beginItem = tempList.get(0);
        }else{
            return null;
        }

        //合同约定相关节点列表
        for (IEPlanBusinessItemSetVO item : calcList){
            //增加起始节点，设置【日期类型】、【合同约定天数】（如果是工作日，则转换为自然日。）
            if(item.getImnum().equals(beginItem.getImnum())){
                //约定期限日期类型（W-工作日/N-自然日）
                item.setCtdtpValue(
                        sdartValueMap.get(lastItem.getCtdtp())
                                .get(0)
                                .getSdval());
                //合同约定期限（天）
                item.setCtdatValue(
                        Integer.valueOf(sdartValueMap.get(lastItem.getCtdat())
                                .get(0)
                                .getSdval()));
                itemList.add(item);
            }

            //如果列表为空，说明已加入起始节点，剩下的节点，增加到截止节点为止。
            if(!ClassUtils.isEmpty(itemList)){
                itemList.add(item);
                if(item.getImnum().equals(lastItem.getImnum())){
                    break;
                }
            }
        }
        return itemList;
    }

    //根据合同约定条款，调整计划节点日期
    private void calcContractStipulate(
            List<IEPlanBusinessItemSetVO> calcList,
            RollPlanDataHelper newHelper,
            Map<String, List<IEPlanSelectValueSet>> sdartValueMap) throws Exception {
        //合同约定相关环节列表
        List<IEPlanBusinessItemSetVO> stipulateItemList = getStipulateItemList(calcList, sdartValueMap);
        //如果无合同约定相关环节，则返回。
        if(ClassUtils.isEmpty(stipulateItemList)){
            return;
        }
        //合同约定起始环节
        IEPlanBusinessItemSetVO firstVO = stipulateItemList.get(0);
        //将环节根据环节ID分组
        Map<String, List<RollPlanItemData>> itemDateMap = newHelper
                .getItemList()
                .stream()
                .collect(Collectors.groupingBy(RollPlanItemData::getImnum));
        //待调整环节列表
        List<RollPlanItemData> pendingItemList = new ArrayList<>();

        //根据【合同约定相关环节列表】，整理出【待调整环节列表】
        stipulateItemList.forEach(s-> pendingItemList.add(itemDateMap.get(s.getImnum()).get(0)));

        //计算合同截止日期，以及合同约定天数
        RollPlanItemData startItem = pendingItemList.get(0);//起始计算节点
        LocalDate from = ClassUtils.StringToLocalDate(startItem.getDtval());
        int during;//合同约定天数（自然日）
        if(IEPlanBusinessItemSetVO.WORK_DAY.equals(firstVO.getCtdtpValue())){
            //根据工作日，换算成自然日。
            during = Long.valueOf(holidayService.getNatureDays(from, firstVO.getCtdatValue())).intValue();
        }else{
            during = firstVO.getCtdatValue();
        }
        LocalDate limitDate = from.plusDays(during);//合同约定截止日期


        //处理发生的节点
        RollPlanItemData lastGItem = startItem;//最后一个取值的节点（如果没有，则为起始节点）
        boolean odue = false;//实际发生日期超过合同约定期限标记（如果超期，则为X）
        Integer sumCaval = 0;//计划能力值合计
        //已发生环节列表
        List<RollPlanItemData> GItemList = new ArrayList<>();
        for(RollPlanItemData i : pendingItemList){
            //如果节点值类型是G-取值
            if(IEPlanBusinessItemSet.GET.equals(i.getCtdtp())){
                //如果已超期，则所有实际发生节点均需要打上超期标记
                if(odue){
                    i.setOdue("X");
                    continue;
                }
                //将当前环节加入已发生环节列表
                lastGItem = i;
                GItemList.add(i);
                //判断是否超期
                LocalDate d = ClassUtils.StringToLocalDate(i.getDtval());
                if(d.isAfter(limitDate)){
                    i.setOdue("X");
                    odue = true;
                }
            }
            //累计计划能力值
            sumCaval = sumCaval + i.getCaval();
        }
        //如果已超期，则不继续进行计算。
        if(odue){
            return;
        }
        //整理待调整列表
        if(!lastGItem.equals(startItem)){
            //如果最后一个取值的节点不是起始节点，则保留最后一个取值节点
            GItemList.remove(GItemList.size()-1);
            //移除所有已发生环节
            pendingItemList.removeAll(GItemList);
        }

        LocalDate startDate = ClassUtils.StringToLocalDate(lastGItem.getDtval());//计算起始日期
        float natureDays = ( limitDate.toEpochDay() - startDate.toEpochDay()) / sumCaval; //日期乘数
        //根据合同约定，调整计划日期
        pendingItemList.forEach(i->{
            int days = (int) natureDays * i.getCaval();
            i.setDtval(startDate.plusDays(days).toString().replace("-",""));
        });
    }
}
