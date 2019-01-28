package com.magustek.szjh.basedataset.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.magustek.szjh.basedataset.dao.CalculateResultDAO;
import com.magustek.szjh.basedataset.entity.CalculateResult;
import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.basedataset.service.CalculateResultService;
import com.magustek.szjh.basedataset.service.IEPlanSelectValueSetService;
import com.magustek.szjh.configset.bean.IEPlanCalculationSet;
import com.magustek.szjh.configset.bean.IEPlanSelectDataSet;
import com.magustek.szjh.configset.service.IEPlanCalculationSetService;
import com.magustek.szjh.configset.service.IEPlanSelectDataSetService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.constant.IEPlanSelectDataConstant;
import com.magustek.szjh.utils.groovy.GroovyBean;
import com.magustek.szjh.utils.groovy.GroovyUtils;
import groovy.lang.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("CalculateResultService")
public class CalculateResultServiceImpl implements CalculateResultService {
    private CalculateResultDAO calculateResultDAO;
    private IEPlanSelectValueSetService iePlanSelectValueSetService;
    private IEPlanSelectDataSetService iePlanSelectDataSetService;
    private IEPlanCalculationSetService iePlanCalculationSetService;
    private int computeCount;//计算次数

    private Map<String, IEPlanSelectDataSet> selectDataSetMap;//取数指标列表

    public CalculateResultServiceImpl(CalculateResultDAO calculateResultDAO, IEPlanSelectValueSetService iePlanSelectValueSetService, IEPlanSelectDataSetService iePlanSelectDataSetService, IEPlanCalculationSetService iePlanCalculationSetService) {
        this.calculateResultDAO = calculateResultDAO;
        this.iePlanSelectValueSetService = iePlanSelectValueSetService;
        this.iePlanSelectDataSetService = iePlanSelectDataSetService;
        this.iePlanCalculationSetService = iePlanCalculationSetService;
    }


    @Override
    public List<CalculateResult> save(List<CalculateResult> list) {
        calculateResultDAO.save(list);
        return list;
    }

    @Override
    public List<CalculateResult> getAllByVersion(String version) {
        return Lists.newArrayList(calculateResultDAO.findAllByVersion(version));
    }

    @Override
    public void deleteAllByVersion(String version) {
        calculateResultDAO.deleteAllByVersion(version);
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<CalculateResult> calculateByVersion(String version) {
        List<CalculateResult> list;
        //设置版本默认值
        if(Strings.isNullOrEmpty(version)){
            version = LocalDate.now().toString();
        }
        //计算指标列表
        List<IEPlanCalculationSet> calculationSetList = iePlanCalculationSetService.getAll();
        //待计算数据
        List<IEPlanSelectValueSet> selectValueSetList = iePlanSelectValueSetService.getAllByVersion(version);
        //获取指标列表（用来匹配指标值类型）
        selectDataSetMap = iePlanSelectDataSetService.getMappedList();

        int size = calculationSetList.size();
        int[] count = {0};
        long l1 = System.currentTimeMillis();
        computeCount = 0;
        List<Object[]> calcList = new ArrayList<>();

        calculationSetList.forEach(c->{
            try {
                log.warn("开始："+c.getCanam());
                calcList.addAll(calc(c, selectValueSetList));
            }catch (Exception e){
                log.error(e.getMessage());
                e.printStackTrace();
            }finally {
                log.warn("完成："+c.getCanam()+"  完成进度："+(++count[0])+"/"+size);
            }

        });
        long l2 = System.currentTimeMillis();
        log.warn("指标组合完成，结果数量：{}，计算次数{}，耗时：{}秒。",calcList.size(), computeCount, ((l2-l1)/1000.00));

        //组装groovy计算条件
        GroovyUtils groovyUtils = new GroovyUtils();
        ArrayList<GroovyBean> beanList = new ArrayList<>();
        calcList.forEach(o->{
            GroovyBean bean = new GroovyBean();
            bean.setBinding((Map<String, Object>) o[0]);
            bean.setCommand((String)o[1]);
            beanList.add(bean);
        });
        //运行计算
        List<GroovyBean> groovyResult = groovyUtils.exec(beanList);
        list = new ArrayList<>(calcList.size());
        //处理计算结果
        for (int i = 0; i < calcList.size(); i++) {
            CalculateResult calculateResult = (CalculateResult) calcList.get(i)[2];
            calculateResult.setCaval(groovyResult.get(i).getResult().toString());
            list.add(calculateResult);
        }

        //清除今天的版本
        deleteAllByVersion(version);
        //保存今天的新版本
        save(list);

        return list;
    }

    private List<Object[]> calc(IEPlanCalculationSet calcSet, List<IEPlanSelectValueSet> selectValueSetList){
        List<Object[]> list = new ArrayList<>();
        //根据htnum分组
        Map<String, List<IEPlanSelectValueSet>> grouped = selectValueSetList
                .stream()
                .collect(Collectors
                        .groupingBy(IEPlanSelectValueSet::getHtnum));
        if(ClassUtils.isEmpty(grouped)){
            return list;
        }
        //根据htnum遍历取数明细列表
        for(Map.Entry<String, List<IEPlanSelectValueSet>> entry : grouped.entrySet()){

            //TODO debug only
            if("52000000000441".equals(entry.getKey())){
                System.out.println("debug point");
            }





            List<IEPlanSelectValueSet> value = entry.getValue();
            //非空判断
            if(ClassUtils.isEmpty(value)){
                continue;
            }

            CalculateResult result = new CalculateResult();
            result.setHtsno(value.get(0).getHtsno());
            result.setHtnum(value.get(0).getHtnum());
            result.setVersion(value.get(0).getVersion());
            result.setCaart(calcSet.getCaart());

            Map<String, Object> binding = new HashMap<>();
            //根据htnum分组遍历取数明细列表，取出所有的指标，及指标值。
            value.forEach(i->{
                String vtype = selectDataSetMap.get(i.getSdart()).getVtype();
                if(!Strings.isNullOrEmpty(i.getSdval())){
                    try {
                        if(IEPlanSelectDataConstant.RESULT_TYPE_DATS.equals(vtype)){
                            binding.put(i.getSdart(), ClassUtils.dfYMD.parse(i.getSdval()));
                        }else{
                            binding.put(i.getSdart(), i.getSdval());
                        }
                    } catch (ParseException e) {
                        log.error(e.getMessage()+JSON.toJSONString(i));
                        e.printStackTrace();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            });
            //使用groovy引擎进行计算，如果抛出【MissingPropertyException】异常，说明单据无该计算值，设置默认为0。
            try{
                //如果计算公式所需变量为空，则跳过计算。
                String[] split = calcSet.getCalcu().split("-");
                boolean breakFlag = false;
                for(String s : split){
                    if(!binding.containsKey(s)){
                        breakFlag = true;
                        break;
                    }
                }
                if(breakFlag){
                    log.debug("跳过计算逻辑，Calcu：{}，variables：{}",calcSet.getCalcu(), JSON.toJSONString(binding));
                    continue;
                }

                list.add(new Object[]{binding,calcSet.getCalcu(),result});
                computeCount++;
            }catch (MissingPropertyException e){
                log.debug(e.getMessage());
                log.debug("htnum:"+result.getHtnum()+"||formula:"+calcSet.getCalcu());
                result.setCaval("0");
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
        return list;
    }
}
