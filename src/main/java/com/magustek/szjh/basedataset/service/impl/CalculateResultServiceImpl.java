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
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
    //private static Lock lock = new ReentrantLock();
    private int computeCount;

    private Map<String, IEPlanSelectDataSet> selectDataSetMap;

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

    @Transactional
    @Override
    public List<CalculateResult> calculateByVersion(String version) {
        List<CalculateResult> list = Collections.synchronizedList(new ArrayList<>());
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
        long begin = System.currentTimeMillis();
        computeCount = 0;

        calculationSetList.forEach(c->{
            //lock.lock();
            try {
                log.warn("开始："+c.getCanam());
                List<CalculateResult> calc = calc(c, selectValueSetList);
                list.addAll(calc);
            }catch (Exception e){
                log.error(e.getMessage());
                e.printStackTrace();
            }finally {
                log.warn("完成："+c.getCanam()+"  完成进度："+(++count[0])+"/"+size);
                //lock.unlock();
            }

        });
        long end = System.currentTimeMillis();

        log.warn("指标计算完成，结果数量：{}，计算次数{}，耗时：{}秒。",list.size(), computeCount, ((end-begin)/1000));

        //清除今天的版本
        deleteAllByVersion(LocalDate.now().toString());
        //保存今天的新版本
        save(list);

        return list;
    }

    private List<CalculateResult> calc(IEPlanCalculationSet calcSet, List<IEPlanSelectValueSet> selectValueSetList){
        List<CalculateResult> list = new ArrayList<>();
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

            Binding binding = new Binding();
            //根据htnum分组遍历取数明细列表，取出所有的指标，及指标值。
            value.forEach(i->{
                String vtype = selectDataSetMap.get(i.getSdart()).getVtype();
                if(!Strings.isNullOrEmpty(i.getSdval())){
                    try {
                        if(IEPlanSelectDataConstant.RESULT_TYPE_DATS.equals(vtype)){

                            //log.error("IEPlanSelectValueSet:"+JSON.toJSONString(i));
                            binding.setVariable(i.getSdart(), ClassUtils.dfYMD.parse(i.getSdval()));
                        }else{
                            binding.setVariable(i.getSdart(), i.getSdval());
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
                Map variables = binding.getVariables();
                String[] split = calcSet.getCalcu().split("-");
                boolean breakFlag = false;
                for(String s : split){
                    if(!variables.containsKey(s)){
                        breakFlag = true;
                        break;
                    }
                }
                if(breakFlag){
                    log.debug("跳过计算逻辑，Calcu：{}，variables：{}",calcSet.getCalcu(), JSON.toJSONString(variables));
                    continue;
                }

                GroovyShell shell = new GroovyShell(binding);
                Object exec = shell.evaluate(calcSet.getCalcu());
                result.setCaval(exec.toString());
                log.debug("计算结果："+JSON.toJSONString(result));
                list.add(result);
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
