package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.*;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.configset.bean.vo.IEPlanReportItemVO;
import com.magustek.szjh.configset.dao.IEPlanReportHeadSetDAO;
import com.magustek.szjh.configset.service.*;
import com.magustek.szjh.plan.utils.PlanConstant;
import com.magustek.szjh.user.bean.CompanyModel;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.KeyValueBean;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("IEPlanReportHeadSetService")
public class IEPlanReportHeadSetServiceImpl implements IEPlanReportHeadSetService {

    private final IEPlanReportHeadSetDAO iePlanReportHeadSetDAO;
    private final IEPlanReportItemSetService iePlanReportItemSetService;
    private final IEPlanOperationSetService iePlanOperationSetService;
    private final OrganizationSetService organizationSetService;
    private final IEPlanStatisticSetService iePlanStatisticSetService;
    private final HttpUtils httpUtils;

    public IEPlanReportHeadSetServiceImpl(IEPlanReportHeadSetDAO iePlanReportHeadSetDAO, IEPlanReportItemSetService iePlanReportItemSetService, IEPlanOperationSetService iePlanOperationSetService, OrganizationSetService organizationSetService, IEPlanStatisticSetService iePlanStatisticSetService, HttpUtils httpUtils) {
        this.iePlanReportHeadSetDAO = iePlanReportHeadSetDAO;
        this.iePlanReportItemSetService = iePlanReportItemSetService;
        this.iePlanOperationSetService = iePlanOperationSetService;
        this.organizationSetService = organizationSetService;
        this.iePlanStatisticSetService = iePlanStatisticSetService;
        this.httpUtils = httpUtils;
    }

    @Override
    public List<IEPlanReportHeadSet> save(List<IEPlanReportHeadSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanReportHeadSetDAO.save(list);
        }else{
            log.error("IEPlanOperationSet 数据为空！");
        }
        return list;
    }

    @Override
    public List<IEPlanReportHeadSet> getAll() {
        return Lists.newArrayList(iePlanReportHeadSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanReportHeadSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanReportHeadSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanReportHeadSet+"?", null, HttpMethod.GET);
        List<IEPlanReportHeadSet> list = OdataUtils.getListWithEntity(result, IEPlanReportHeadSet.class);
        //清除现有数据
        deleteAll();
        //保存新数据
        save(list);
        return list;
    }

    @Override
    public IEPlanReportHeadVO getReportConfigByBukrs(String bukrs, String rptyp, String dmart, String rpdat) throws Exception {
        IEPlanReportHeadVO vo = getReportConfigByBukrs(bukrs, rptyp);
        //将传入的时间转化为LocalDate类型
        LocalDate startDate;
        //计划维度单位-年、月、季、日
        if("Y".equals(vo.getPunit())){
            if("X".equals(vo.getTflag())){
                //绝对时间
                startDate = LocalDate.parse(rpdat+"-01-01");
            }else{
                //相对时间
                startDate = LocalDate.now();
            }
        }else if("M".equals(vo.getPunit())) {
            if("X".equals(vo.getTflag())){
                //绝对时间
                if(vo.getRptyp().equals("MR")){
                    //月报
                    startDate = LocalDate.parse(rpdat+"-01");
                }else {
                    //年报
                    startDate = LocalDate.parse(rpdat+"-01-01");
                }

            }else{
                //相对时间
                startDate = LocalDate.now();
            }
        }else{
            startDate = LocalDate.parse(rpdat);
        }
        //初始化x、y、z轴数据
        //用户如果编制部门报表，需要根据dmart标记对组织进行筛选。
        axisType(vo, dmart,startDate);
        return vo;
    }

    @Override
    public IEPlanReportHeadVO getReportConfigByBukrs(String bukrs, String rptyp) throws Exception {
        //获取报表配置抬头
        IEPlanReportHeadSet ar = iePlanReportHeadSetDAO.findByBukrsAndRptyp(bukrs, rptyp);
        IEPlanReportHeadVO vo = new IEPlanReportHeadVO();
        BeanUtils.copyProperties(ar, vo);
        //获取报表配置明细
        List<IEPlanReportItemSet> item = iePlanReportItemSetService.getByBukrsAndRptyp(bukrs, rptyp);
        List<IEPlanReportItemVO> itemVOList = new ArrayList<>();
        //获取指标描述
        Map<String, String> zbnamMap = iePlanOperationSetService.getZbnamMap();

        item.forEach(i->{
            IEPlanReportItemVO itemVO = new IEPlanReportItemVO();
            BeanUtils.copyProperties(i, itemVO);
            itemVO.setZbnam(zbnamMap.get(i.getZbart()));
            itemVOList.add(itemVO);
        });
        vo.setItemVOS(itemVOList);
        return vo;
    }
    //初始化x、y、z轴数据
    private void axisType(IEPlanReportHeadVO vo, String dmart, LocalDate startDate) throws Exception{
        ArrayList<String> axis = new ArrayList<>(3);
        ArrayList<ArrayList<KeyValueBean>> value = new ArrayList<>(3);
        axis.add(vo.getXaxis());
        axis.add(vo.getYaxis());
        axis.add(vo.getZaxis());


        for(String s : axis){
            switch (s){
                case PlanConstant.AXIS_ORG:
                    value.add(organizationSetService.getORG(vo.getBukrs(), vo.getDmart(), dmart));
                    break;
                case PlanConstant.AXIS_TIM:
                    value.add(getTIM(startDate, vo));
                    break;
                case PlanConstant.AXIS_ZB:
                    value.add(getZB(vo));
                    break;
                default :
                    log.error("axis error:" + s);
            }
        }

        vo.setXvalue(value.get(0));
        vo.setYvalue(value.get(1));
        vo.setZvalue(value.get(2));
    }

    //返回指定日期列表
    private ArrayList<KeyValueBean> getTIM(LocalDate rpdat, IEPlanReportHeadVO vo){

        String punit = vo.getPunit();
        String pvalue = vo.getPvalu();
        String bukrs = vo.getBukrs();
        String rptyp = vo.getRptyp();
        //获取【小计】指标
        ArrayList<KeyValueBean> t800 = new ArrayList<>();

        int i = Integer.parseInt(pvalue);
        int firstIndex = 0;
        ArrayList<KeyValueBean> keyValueBeans = new ArrayList<>();

        //获取统计指标配置数据，统计指标需要加在时间轴前面
        List<IEPlanStatisticSet> statisticSetList = iePlanStatisticSetService
                .getAllByBukrsAndRptyp(bukrs, rptyp);
        statisticSetList.forEach(st->{
            KeyValueBean bean = new KeyValueBean();
            bean.put(st.getTmart(), st.getTmnam(), "S");//统计指标不可编辑
            keyValueBeans.add(bean);

            if(bean.getKey().equals("T800")){
                t800.add(bean);
            }
        });

        //StringBuilder strBuilder = new StringBuilder();
        for (;i>=0;i--){
            KeyValueBean item = new KeyValueBean();
            String date = ClassUtils.formatDate(rpdat, punit);
            if("MR".equals(rptyp)){
                item.put(date, date, "H");
            }else{
                item.put(date, date, "M");
            }

            //strBuilder.append(date).append("+");

            keyValueBeans.add(item);
            rpdat = ClassUtils.getDate(rpdat, punit, 1, true);

            if(firstIndex == 0){
                firstIndex = keyValueBeans.size();
            }
        }

        //月报时间处理
        if(vo.getRptyp().equals("MR")){
            KeyValueBean firstBean = keyValueBeans.get(firstIndex-1);
            KeyValueBean lastBean = keyValueBeans.get(keyValueBeans.size() - 1);

            //自定义标识，第一个时间段后面加个空格，方便判断
            firstBean.put(firstBean.getKey()+" ", firstBean.getValue()+" ");

            //自定义标识，最后一个节点需要增加【后】--2019年1月后
            lastBean.put(lastBean.getKey()+"后", lastBean.getValue()+"后");
        }else{
            //去掉最后一个节点
            keyValueBeans.remove(keyValueBeans.size()-1);
        }
        //【小计】公式计算
        StringBuilder calc = new StringBuilder();
        //首次计算标记
        boolean first = true;
        for (KeyValueBean keyValueBean : keyValueBeans) {
            if(ClassUtils.isEmpty(t800)){
                break;
            }
            //统计指标不参与计算
            if(!"S".equals(keyValueBean.getOpera())){
                if(first){
                    calc = new StringBuilder(t800.get(0).getKey()+"=$"+keyValueBean.getKey()+"$");
                    first = false;
                }else{
                    calc.append("+$").append(keyValueBean.getKey()).append("$");
                }
            }
        }
        t800.get(0).setCalc(calc.toString());

        return keyValueBeans;
    }
    //获取所有经营指标分类
    private ArrayList<KeyValueBean> getZB(IEPlanReportHeadVO vo){
        List<IEPlanReportItemVO> list = vo.getItemVOS();
        ArrayList<KeyValueBean> keyValueBeans = new ArrayList<>(list.size());
        list.forEach(item->{
            KeyValueBean bean = new KeyValueBean();
            bean.put(item.getZbart(), item.getZbnam(), item.getOpera());
            keyValueBeans.add(bean);
        });
        return keyValueBeans;
    }
}
