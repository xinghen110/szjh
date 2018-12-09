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

import javax.transaction.Transactional;
import java.time.LocalDate;
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
    public List<IEPlanSelectValueSet> getAllByVersion(String version) {
        return Lists.newArrayList(iePlanSelectValueSetDAO.findAllByVersion(version));
    }

    @Override
    public void deleteAllByVersion(String version) {
        iePlanSelectValueSetDAO.deleteAllByVersion(version);
    }

    public List<IEPlanSelectValueSet> getAllFromDatasource(String begin, String end, String bukrs) {
        List<IEPlanSelectValueSet> list = new ArrayList<>();
        //获取所有取数指标
        List<IEPlanSelectDataSet> selectDataSetList = iePlanSelectDataSetService.getAll();
        //根据取数指标循环取数
        selectDataSetList.forEach(selectDataSet -> list.addAll(getAllFromDatasource(begin,end,bukrs,selectDataSet)));
        return list;
    }

    //根据组织机构配置表中的配置，取出所有待处理的公司及其取数时间范围
    @Transactional
    @Override
    public List<IEPlanSelectValueSet> fetchData() throws Exception {
        List<IEPlanSelectValueSet> list = new ArrayList<>();
        List<KeyValueBean> reportList = organizationSetService.getRangeList();

        reportList.parallelStream().forEach(item->list.addAll(getAllFromDatasource(item.getValue(), item.getOpera(), item.getKey())));
        //清除今天的版本
        deleteAllByVersion(LocalDate.now().toString());
        //保存新的今天版本
        save(list);
        return list;
    }

    private List<IEPlanSelectValueSet> getAllFromDatasource(String begin, String end, String bukrs, IEPlanSelectDataSet selectDataSet) {
        String url = OdataUtils.IEPlanSelectValueSet
                +"?$filter=sdart eq '"+selectDataSet.getSdart()+"' " +
                "and bukrs eq '"+bukrs+"' " +
                "and begda ge datetime'"+begin+"T00:00:00' " +
                "and endda le datetime'"+end+"T23:59:59' ";
        String result = httpUtils.getResultByUrl(url, null, HttpMethod.GET);

        try {
            List<IEPlanSelectValueSet> list = OdataUtils.getListWithEntity(result,
                    IEPlanSelectValueSet.class);
            list.forEach(item->{
                String value = item.getSdval().trim();
                //判断如果值的类型是日期，需要进行非空处理
                if(IEPlanSelectDataConstant.RESULT_TYPE_DATS.equals(selectDataSet.getVtype())){
                    item.setSdval(value.equals("00000000")?"":value);
                }else{
                    item.setSdval(value);
                }

                item.setSdart(selectDataSet.getSdart());
                item.setBegda(begin);
                item.setEndda(end);
                item.setVersion(LocalDate.now().toString());
            });
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
