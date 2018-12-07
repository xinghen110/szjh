package com.magustek.szjh.basedataset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.basedataset.dao.IEPlanDimenValueSetDAO;
import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;
import com.magustek.szjh.basedataset.service.IEPlanDimenValueSetService;
import com.magustek.szjh.configset.bean.IEPlanDimensionSet;
import com.magustek.szjh.configset.service.IEPlanDimensionSetService;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.utils.KeyValueBean;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.constant.IEPlanDimensionSetConstant;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("IEPlanDimenValueSetService")
public class IEPlanDimenValueSetServiceImpl implements IEPlanDimenValueSetService {

    private final HttpUtils httpUtils;
    private final IEPlanDimenValueSetDAO iePlanDimenValueSetDAO;
    private final IEPlanDimensionSetService iePlanDimensionSetService;
    private final OrganizationSetService organizationSetService;

    public IEPlanDimenValueSetServiceImpl(HttpUtils httpUtils, IEPlanDimenValueSetDAO iePlanDimenValueSetDAO, IEPlanDimensionSetService iePlanDimensionSetService, OrganizationSetService organizationSetService) {
        this.httpUtils = httpUtils;
        this.iePlanDimenValueSetDAO = iePlanDimenValueSetDAO;
        this.iePlanDimensionSetService = iePlanDimensionSetService;
        this.organizationSetService = organizationSetService;
    }

    @Override
    public List<IEPlanDimenValueSet> save(List<IEPlanDimenValueSet> list) {
        iePlanDimenValueSetDAO.save(list);
        return list;
    }

    @Override
    public List<IEPlanDimenValueSet> getAll() {
        return Lists.newArrayList(iePlanDimenValueSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanDimenValueSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanDimenValueSet> getAllFromDatasource(String begin, String end, String bukrs) throws Exception {
        List<IEPlanDimenValueSet> list = new ArrayList<>();
        //获取所有取数指标
        List<IEPlanDimensionSet> dimensionSetList = iePlanDimensionSetService.getAll();
        //根据取数指标循环取数
        dimensionSetList.forEach(dimensionSet -> list.addAll(getAllFromDatasource(begin,end,bukrs,dimensionSet)));
        return list;
    }

    @Override
    public List<IEPlanDimenValueSet> fetchData() throws Exception {
        List<IEPlanDimenValueSet> list = new ArrayList<>();
        List<KeyValueBean> reportList = organizationSetService.getRangeList();
        for (KeyValueBean item : reportList){
            list.addAll(getAllFromDatasource(item.getValue(), item.getOpera(), item.getKey()));
        }
        iePlanDimenValueSetDAO.deleteAll();
        iePlanDimenValueSetDAO.save(list);
        return list;
    }

    private List<IEPlanDimenValueSet> getAllFromDatasource(String begin, String end, String bukrs, IEPlanDimensionSet dimensionSet){
        String url = OdataUtils.IEPlanDimenValueSet
                +"?$filter=dmart eq '"+dimensionSet.getDmart()+"' " +
                "and bukrs eq '"+bukrs+"' " +
                "and begda ge datetime'"+begin+"T00:00:00' " +
                "and endda le datetime'"+end+"T23:59:59' ";
        String result = httpUtils.getResultByUrl(url, null, HttpMethod.GET);

        //如果指标返回类型是日期，则需要通过keys进行处理
        String[] keys = IEPlanDimensionSetConstant.RESULT_TYPE_DATS.equals(dimensionSet.getDmtyp()) ?
                new String[]{dimensionSet.getDmart()}:
                null;

        try {
            List<IEPlanDimenValueSet> list = OdataUtils.getListWithEntity(result,
                    IEPlanDimenValueSet.class,
                    keys,
                    "yyyy-MM-dd");

            list.forEach(item->{
                item.setDmval(item.getDmval().trim());
                item.setDmart(dimensionSet.getDmart());
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
