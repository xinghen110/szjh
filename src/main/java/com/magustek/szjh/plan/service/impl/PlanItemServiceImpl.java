package com.magustek.szjh.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.configset.bean.vo.IEPlanReportItemVO;
import com.magustek.szjh.configset.service.IEPlanOperationSetService;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.PlanItem;
import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import com.magustek.szjh.plan.bean.vo.PlanItemVO;
import com.magustek.szjh.plan.dao.PlanItemDAO;
import com.magustek.szjh.plan.dao.PlanLayoutDAO;
import com.magustek.szjh.plan.service.PlanItemService;
import com.magustek.szjh.plan.service.RollPlanArchiveService;
import com.magustek.szjh.plan.utils.PlanConstant;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.KeyValueBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("PlanItemService")
public class PlanItemServiceImpl implements PlanItemService {
    private PlanItemDAO planItemDAO;
    private PlanLayoutDAO planLayoutDAO;
    private OrganizationSetService organizationSetService;
    private IEPlanOperationSetService iePlanOperationSetService;
    private RollPlanArchiveService rollPlanArchiveService;


    public PlanItemServiceImpl(PlanItemDAO planItemDAO, PlanLayoutDAO planLayoutDAO, OrganizationSetService organizationSetService, IEPlanOperationSetService iePlanOperationSetService, RollPlanArchiveService rollPlanArchiveService) {
        this.planItemDAO = planItemDAO;
        this.planLayoutDAO = planLayoutDAO;
        this.organizationSetService = organizationSetService;
        this.iePlanOperationSetService = iePlanOperationSetService;
        this.rollPlanArchiveService = rollPlanArchiveService;
    }

    //根据ID更新指标值
    @Override
    public PlanItem[] save(PlanItem[] items) throws Exception{
        for(int i=0;i<items.length;i++) {
            PlanItem item = items[i];
            Long id = item.getId();
            PlanItem planItem = planItemDAO.findOne(id);
            try{
                new BigDecimal(item.getZbval());
            }catch (Exception e){
                throw new Exception("数据格式错误："+item.getZbval());
            }
            planItem.setZbval(item.getZbval());
            items[i] = planItemDAO.save(planItem);
        }
        return items;
    }

    @Override
    public List<PlanItem> save(List<PlanItem> list) {
        return Lists.newArrayList(planItemDAO.save(list));
    }

    @Override
    public List<PlanItem> getListByHeaderId(PlanItemVO vo) throws Exception{
        String zaxis = vo.getZaxis();
        String zvalue = vo.getZvalue();
        List<PlanItem> list;
        switch (zaxis){
            case PlanConstant.AXIS_TIM:
                list = planItemDAO.findAllByHeaderIdAndAndZtval(vo.getHeaderId(), zvalue);
                break;
            case PlanConstant.AXIS_ORG:
                list = planItemDAO.findAllByHeaderIdAndAndDmval(vo.getHeaderId(), zvalue);
                break;
            case PlanConstant.AXIS_ZB:
                list = planItemDAO.findAllByHeaderIdAndAndZbart(vo.getHeaderId(), zvalue);
                break;
            default:
                throw new Exception("Y轴类型错误！");
        }
        return list;
    }

    @Override
    public List<PlanItem> delete(List<PlanItem> list) {
        planItemDAO.delete(list);
        return list;
    }

    @Override
    public void deleteByHeaderId(Long headerId) {
        planItemDAO.deleteAllByHeaderId(headerId);
    }

    @Override
    public List<PlanItem> coverVOToList(PlanItemVO vo){
        //获取报表配置数据
        IEPlanReportHeadVO config = JSON.parseObject(planLayoutDAO.findTopByHeaderId(vo.getHeaderId()).getLayout(), IEPlanReportHeadVO.class);
        String xaxis = config.getXaxis();
        KeyValueBean[] xvalue = (KeyValueBean[])config.getXvalue().toArray();
        String yaxis = config.getYaxis();
        KeyValueBean[] yvalue = (KeyValueBean[])config.getYvalue().toArray();
        String zaxis = config.getZaxis();

        String[][] value = vo.getValue();
        List<PlanItem> itemList = new ArrayList<>(this.getArrayCount(value));
        //根据x轴、y轴配置，转换bean
        for(int yIndex=0;yIndex<value.length;yIndex++){
            String[] subValue = value[yIndex];
            for(int xIndex=0;xIndex<subValue.length;xIndex++){
                PlanItem item = new PlanItem();
                item.setHeaderId(vo.getHeaderId());
                item.setId(vo.getItemId()[yIndex][xIndex]);
                //z轴
                setPlanItemValue(item, zaxis, vo.getZaxis(), vo.getZvalue(), subValue[xIndex]);
                //x轴
                setPlanItemValue(item, xaxis, xvalue[xIndex].getKey(), xvalue[xIndex].getValue(), subValue[xIndex]);
                //y轴
                setPlanItemValue(item, yaxis, yvalue[yIndex].getKey(), yvalue[yIndex].getValue(), subValue[xIndex]);
                itemList.add(item);
            }
        }

        return itemList;
    }

    /** 根据数据库查出的数据，转换为前端使用的数据。
     * @param list      根据z轴值查出来的列表（重点：z轴值固定）
     * @param zValue    z轴值
     * @return          vo
     * @throws Exception    报错
     */
    @Override
    public PlanItemVO coverListToVO(List<PlanItem> list, String zValue) throws Exception{

        PlanItemVO vo = new PlanItemVO();
        if(ClassUtils.isEmpty(list)){
            return vo;
        }

        //获取报表配置数据
        IEPlanReportHeadVO config = JSON.parseObject(planLayoutDAO.findTopByHeaderId(list.get(0).getHeaderId()).getLayout(), IEPlanReportHeadVO.class);
        String yaxis = config.getYaxis();
        //获取y轴key列表
        ArrayList<String> yvalue = (ArrayList<String>)config.getYvalue().stream()
                .map(KeyValueBean::getKey)
                .collect(Collectors.toList());

        vo.setZaxis(config.getZaxis());
        vo.setZvalue(zValue);

        // 按照y轴分组
        Map<String, List<PlanItem>> groupResult;
        switch (yaxis){
            case PlanConstant.AXIS_TIM:
                groupResult = list.stream()
                        .collect(Collectors.groupingBy(PlanItem::getZtval));
                break;
            case PlanConstant.AXIS_ORG:
                groupResult = list.stream()
                        .collect(Collectors.groupingBy(PlanItem::getDmval));
                break;
            case PlanConstant.AXIS_ZB:
                groupResult = list.stream()
                        .collect(Collectors.groupingBy(PlanItem::getZbart));
                break;
            default:
                throw new Exception("y轴错误！");
        }

        String[][] valueList = new String[groupResult.entrySet().size()][];
        Long[][] idList = new Long[groupResult.entrySet().size()][];
        for(Map.Entry<String, List<PlanItem>> item : groupResult.entrySet()){
            List<PlanItem> row = item.getValue();
            //由于返回数据是map类型，会导致无序，根据y轴key值进行排序
            String key = item.getKey();
            int index = yvalue.indexOf(key);
            List<String> rowArray;
            List<Long> rowKeys;
            //取出指标数据及其ID
            rowArray = row.stream().map(PlanItem::getZbval).collect(Collectors.toList());
            rowKeys = row.stream().map(PlanItem::getId).collect(Collectors.toList());

            idList[index] = rowKeys.toArray( new Long[1]);
            valueList[index] = rowArray.toArray( new String[1]);
        }
        vo.setValue(valueList);
        vo.setItemId(idList);
        return vo;
    }
    //按照map格式返回列表数据
    @Override
    public List<Map<String, String>> coverListToMap(List<PlanItem> list) throws Exception {
        List<Map<String, String>> resultList;
        //获取headerId
        Long headerId = list.get(0).getHeaderId();
        //获取布局数据
        IEPlanReportHeadVO layout = JSON.parseObject(planLayoutDAO.findTopByHeaderId(headerId).getLayout(), IEPlanReportHeadVO.class);
        Map<String, List<PlanItem>> group;
        //根据Y轴分组
        switch (layout.getYaxis()){
            case PlanConstant.AXIS_TIM :
                group = list.stream()
                        .collect(Collectors.groupingBy(PlanItem::getZtval, LinkedHashMap::new, Collectors.toList()));
                break;
            case PlanConstant.AXIS_ORG :
                group = list.stream()
                        .collect(Collectors.groupingBy(PlanItem::getDmval, LinkedHashMap::new, Collectors.toList()));
                break;
            case PlanConstant.AXIS_ZB :
                group = list.stream()
                        .collect(Collectors.groupingBy(PlanItem::getZbart, LinkedHashMap::new, Collectors.toList()));
                break;
            default:
                throw new Exception("axis轴错误："+layout.getZaxis());
        }
        resultList = handleMap(layout, group);

        return resultList;
    }

    @Override
    public Map<String, BigDecimal> getZBValByHeaderId(Long headerId){
        //获取这个单子的所有数据
        List<PlanItem> itemList = planItemDAO.findAllByHeaderId(headerId);
        //结果按照指标字段分组
        Map<String, List<PlanItem>> group = itemList.stream().collect(Collectors.groupingBy(PlanItem::getZbart));
        Map<String, BigDecimal> result = new HashMap<>(group.keySet().size());
        for( Map.Entry<String, List<PlanItem>> item : group.entrySet()){
            List<PlanItem> valueList = item.getValue();
            BigDecimal decimal = new BigDecimal(0);
            for(PlanItem planItem : valueList){
                decimal = decimal.add(new BigDecimal(planItem.getZbval()));
            }
            result.put(item.getKey(), decimal);
        }
        return result;
    }

    //创建计划抬头的同时初始化明细数据
    @Override
    public List<PlanItem> initItemDataByConfig(IEPlanReportHeadVO config, Long headerId) throws Exception {
        //配置数据
        String xaxis = config.getXaxis();
        ArrayList<KeyValueBean> xvalue = config.getXvalue();
        String yaxis = config.getYaxis();
        ArrayList<KeyValueBean> yvalue = config.getYvalue();
        String zaxis = config.getZaxis();
        ArrayList<KeyValueBean> zvalue = config.getZvalue();
        //初始化变量
        int size = xvalue.size()*yvalue.size()*zvalue.size();
        List<PlanItem> itemList = new ArrayList<>(size);
        //填充初始化数据
        String[] axis = {xaxis,yaxis,zaxis};
        for(KeyValueBean xBean : xvalue){
            for(KeyValueBean yBean : yvalue){
                for(KeyValueBean zBean : zvalue){
                    KeyValueBean[] value = {xBean,yBean,zBean};
                    itemList.add(constructItem(axis, value, config, headerId));
                }
            }
        }
        return itemList;
    }

    @Override
    public void initCalcData(List<PlanItem> itemList,
                                       IEPlanReportHeadVO config,
                                       PlanHeader planHeader) throws Exception {
        //复制数据到【roll_plan_head_data_archive】、【roll_plan_item_data_archive】表
        rollPlanArchiveService.copyData(planHeader);
        //获取滚动计划列表，并根据经营指标值分组
        Map<String, List<RollPlanHeadDataArchive>> headMapByZbart = rollPlanArchiveService
                .getHeadDataArchiveList(planHeader)
                .stream()
                .filter(i-> i.getZbart() != null) //容错
                .collect(Collectors.groupingBy(RollPlanHeadDataArchive::getZbart));
        log.warn("滚动计划列表获取完成");
        //将统计数据存入报表项目
        //取出待统计的指标list(操作方式为【S】)
        List<String> zbartList = config
                .getItemVOS()
                .stream()
                .filter(item -> item.getOpera().equals("S"))
                .map(IEPlanReportItemVO::getZbart)
                .collect(Collectors.toList());

        log.warn("指标列表，数量{}",zbartList.size());
        if(ClassUtils.isEmpty(zbartList)){
            return;
        }

        //将计划明细根据经营指标分组
        Map<String, List<PlanItem>> itemMapByZbart = itemList
                .stream()
                .collect(Collectors.groupingBy(PlanItem::getZbart));
        log.warn("将计划明细根据经营指标分组");
        itemMapByZbart.forEach((k, v)->{
            //如果当前数据的经营指标需要统计
            if(zbartList.contains(k)){
                //当前经营指标的计划列表
                List<RollPlanHeadDataArchive> headList = headMapByZbart.get(k);
                v.forEach(item->{
                    //报表维度，格式：D110:50003521
                    String dmval = item.getDmart()+":"+item.getDmval();
                    if(ClassUtils.isEmpty(headList)){
                        return;
                    }
                    headList.forEach(head->{
                        //如果维度相同，滚动计划时间如果在item日期范围内，则加总金额
                        if(!Strings.isNullOrEmpty(head.getDmval())
                                && head.getDmval().contains(dmval)
                                && !item.getZtval().startsWith("T")){
                            calcItem(item, head);
                        }
                    });
                });
            }
        });
    }

    //根据指标分组统计计划的zbval值
    @Override
    public ArrayList<KeyValueBean> getZbList(Long headerId) {
        List<Object[]> zbList = planItemDAO.zbvalListByHeaderIdGroupByZbart(headerId);
        ArrayList<KeyValueBean> list = new ArrayList<>(zbList.size());
        for (Object[] s : zbList){
            KeyValueBean bean = new KeyValueBean();
            bean.put(s[1].toString(), new BigDecimal(s[0].toString()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            list.add(bean);
        }
        return list;
    }

    //初始化明细数据
    private PlanItem constructItem(String[] axis, KeyValueBean[] value, IEPlanReportHeadVO config, Long headerId) throws Exception{
        PlanItem item = new PlanItem();
        item.setHeaderId(headerId);
        for(int i=0;i<axis.length;i++){
            switch (axis[i]){
                case PlanConstant.AXIS_TIM:
                    item.setZtime(config.getPunit());
                    item.setZtval(value[i].getKey());
                    break;
                case PlanConstant.AXIS_ORG:
                    item.setDmval(value[i].getKey());
                    item.setDmart(config.getDmart());
                    break;
                case PlanConstant.AXIS_ZB:
                    item.setZbart(value[i].getKey());
                    item.setZbval("0");//初始化指标值为0
                    break;
                default:
                    throw new Exception("axis轴错误："+axis[i]);
            }
        }
        return item;
    }

    private Integer getArrayCount(Object[][] o){
        Integer x;
        Integer y;
        if(o!=null){
            x = o.length;
            if(o[0] != null){
                y = o[0].length;
                return x*y;
            }
        }
        return 0;
    }

    private void setPlanItemValue(PlanItem item, String type, String key, String value, String zbval){
        switch (type){
            case PlanConstant.AXIS_ORG:
                item.setDmart(key);
                item.setDmval(value);
                break;
            case PlanConstant.AXIS_TIM:
                item.setZtime(key);
                break;
            case PlanConstant.AXIS_ZB:
                item.setZbart(key);
                //报表值存入zbval字段
                item.setZbval(zbval);
                break;
        }
    }

    private List<Map<String, String>> handleMap(IEPlanReportHeadVO layout, Map<String, List<PlanItem>> group) throws Exception {
        List<Map<String, String>> resultList = new ArrayList<>(group.size());
        List<PlanItem> pList;

        Map<String, String> orgKeyValue = organizationSetService.orgKeyValue();
        Map<String, String> zbnamMap = iePlanOperationSetService.getZbnamMap();
        //按行处理数据
        for(Map.Entry<String, List<PlanItem>> i : group.entrySet()) {
            Map<String, String> map = new HashMap<>();
            pList = i.getValue();
            //处理每个单元格数据
            for (PlanItem p : pList) {
                switch (layout.getXaxis()) {
                    case PlanConstant.AXIS_TIM:
                        map.put(p.getZtval(), p.getZbval());
                        map.put(p.getZtval() + "_id", p.getId().toString());
                        break;
                    case PlanConstant.AXIS_ORG:
                        //如果是组织，需要转换成中文描述
                        map.put(orgKeyValue.get(p.getDmval()), p.getZbval());
                        map.put(orgKeyValue.get(p.getDmval()) + "_id", p.getId().toString());
                        break;
                    case PlanConstant.AXIS_ZB:
                        //如果是指标，需要转换成中文描述
                        map.put(zbnamMap.get(p.getZbart()), p.getZbval());
                        map.put(zbnamMap.get(p.getZbart()) + "_id", p.getId().toString());
                        break;
                }
            }
            //设置y轴key以及描述
            map.put("y_key",i.getKey());
            switch (layout.getYaxis()) {
                case PlanConstant.AXIS_TIM:
                    map.put("y_name",i.getKey());
                    break;
                case PlanConstant.AXIS_ORG:
                    map.put("y_name",orgKeyValue.get(i.getKey()));
                    break;
                case PlanConstant.AXIS_ZB:
                    map.put("y_name",zbnamMap.get(i.getKey()));
                    break;
            }
            resultList.add(map);
        }
        return resultList;
    }

    private void calcItem(PlanItem item, RollPlanHeadDataArchive head) {
        String ztval = item.getZtval();
        String ztime = item.getZtime();
        long headTime;
        try {
            headTime = ClassUtils.StringToLocalDate(head.getDtval()).toEpochDay();
        } catch (Exception e) {
            return;
        }

        log.warn("计划明细计算：历史维度单位——{}，明细时间——{}，滚动计划日期——{}，滚动计划金额——{}",
                ztime,
                ztval,
                head.getDtval(),
                head.getWears().toString());
        long start;
        long end;
        String startTime = "2000-01-01";
        String endTime = "2199-12-31";

        switch (ztime){
            case PlanItem.ZTIME_Y:
                //计划起始日期，需包含以前的计划
                if(!ztval.contains(" ")){
                    startTime = ztval + "-01-01";
                }
                //计划结束日期，需包含以后的计划
                if(!ztval.contains("后")){
                    endTime = ztval + "-12-31";
                }

                start = LocalDate.parse(startTime).toEpochDay();
                end = LocalDate.parse(endTime).toEpochDay();
                break;
            case PlanItem.ZTIME_M:
                //计划起始日期，需包含以前的计划
                if(ztval.contains(" ")){
                    startTime = "2000-01-01";
                    start = LocalDate.parse(startTime).toEpochDay();
                    end = LocalDate.parse(ztval.replace(" ","")+"-01").with(TemporalAdjusters.lastDayOfMonth()).toEpochDay();
                }else if(ztval.contains("后")){
                    //计划结束日期，需包含以后的计划
                    start = LocalDate.parse(ztval.replace("后", "")+"-01").toEpochDay();
                    endTime = "2199-12-31";
                    end = LocalDate.parse(endTime).toEpochDay();
                }else{
                    start = LocalDate.parse(ztval+"-01").toEpochDay();
                    end = LocalDate.parse(ztval+"-01").with(TemporalAdjusters.lastDayOfMonth()).toEpochDay();
                }
                break;
            default:
                return;
        }
        log.warn("起始时间{}，截止时间{}，计划时间{}",
                start,
                end,
                headTime);
        //如果计划日期在报表日期范围内，则汇总金额
        if(start <= headTime && headTime <= end){
            BigDecimal zbval = new BigDecimal(item.getZbval());
            item.setZbval(zbval.add(head.getWears()).toString());
        }
    }
}
