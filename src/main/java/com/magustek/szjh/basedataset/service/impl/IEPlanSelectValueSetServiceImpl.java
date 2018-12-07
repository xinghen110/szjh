package com.magustek.szjh.basedataset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.basedataset.dao.IEPlanSelectValueSetDAO;
import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.basedataset.service.IEPlanSelectValueSetService;
import com.magustek.szjh.configset.bean.IEPlanSelectDataSet;
import com.magustek.szjh.configset.service.IEPlanSelectDataSetService;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.utils.KeyValueBean;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.constant.IEPlanSelectDataConstant;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("IEPlanSelectValueSetService")
public class IEPlanSelectValueSetServiceImpl implements IEPlanSelectValueSetService {
    private final HttpUtils httpUtils;
    private final IEPlanSelectValueSetDAO iePlanSelectValueSetDAO;
    private final IEPlanSelectDataSetService iePlanSelectDataSetService;
    private final OrganizationSetService organizationSetService;

    public IEPlanSelectValueSetServiceImpl(HttpUtils httpUtils, IEPlanSelectValueSetDAO iePlanSelectValueSetDAO, IEPlanSelectDataSetService iePlanSelectDataSetService, OrganizationSetService organizationSetService) {
        this.httpUtils = httpUtils;
        this.iePlanSelectValueSetDAO = iePlanSelectValueSetDAO;
        this.iePlanSelectDataSetService = iePlanSelectDataSetService;
        this.organizationSetService = organizationSetService;
    }

    @Override
    public List<IEPlanSelectValueSet> save(List<IEPlanSelectValueSet> list) {
        iePlanSelectValueSetDAO.save(list);
        return list;
    }

    @Override
    public List<IEPlanSelectValueSet> getAll() {
        return Lists.newArrayList(iePlanSelectValueSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanSelectValueSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanSelectValueSet> getAllFromDatasource(String begin, String end, String bukrs) {
        List<IEPlanSelectValueSet> list = new ArrayList<>();
        //获取所有取数指标
        List<IEPlanSelectDataSet> selectDataSetList = iePlanSelectDataSetService.getAll();
        //根据取数指标循环取数
        selectDataSetList.forEach(selectDataSet -> list.addAll(getAllFromDatasource(begin,end,bukrs,selectDataSet)));
        return list;
    }

    //根据组织机构配置表中的配置，取出所有待处理的公司及其取数时间范围
    @Override
    public List<IEPlanSelectValueSet> fetchData() throws Exception {
        List<IEPlanSelectValueSet> list = new ArrayList<>();
        List<KeyValueBean> reportList = organizationSetService.getRangeList();

        reportList.parallelStream().forEach(item->list.addAll(getAllFromDatasource(item.getValue(), item.getOpera(), item.getKey())));
        //清除今天的版本
        iePlanSelectValueSetDAO.deleteAll();
        //保存新的今天版本
        iePlanSelectValueSetDAO.save(list);
        return list;
    }

    private List<IEPlanSelectValueSet> getAllFromDatasource(String begin, String end, String bukrs, IEPlanSelectDataSet selectDataSet) {
        String url = OdataUtils.IEPlanSelectValueSet
                +"?$filter=sdart eq '"+selectDataSet.getSdart()+"' " +
                "and bukrs eq '"+bukrs+"' " +
                "and begda ge datetime'"+begin+"T00:00:00' " +
                "and endda le datetime'"+end+"T23:59:59' ";
        String result = httpUtils.getResultByUrl(url, null, HttpMethod.GET);

        //如果指标返回类型是日期，则需要通过keys进行处理
        String[] keys = IEPlanSelectDataConstant.RESULT_TYPE_DATS.equals(selectDataSet.getVtype()) ?
                new String[]{selectDataSet.getSdart()}:
                null;

        try {
            List<IEPlanSelectValueSet> list = OdataUtils.getListWithEntity(result,
                    IEPlanSelectValueSet.class,
                    keys,
                    "yyyy-MM-dd");
            list.forEach(item->{
                item.setSdval(item.getSdval().trim());
                item.setSdart(selectDataSet.getSdart());
                item.setBegda(begin);
                item.setEndda(end);
            });
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
