package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanDimensionSet;
import com.magustek.szjh.configset.dao.IEPlanDimensionSetDAO;
import com.magustek.szjh.configset.service.IEPlanDimensionSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("IEPlanDimensionSetService")
public class IEPlanDimensionSetServiceImpl implements IEPlanDimensionSetService {
    private final HttpUtils httpUtils;
    private final IEPlanDimensionSetDAO iePlanDimensionSetDAO;

    public IEPlanDimensionSetServiceImpl(HttpUtils httpUtils, IEPlanDimensionSetDAO iePlanDimensionSetDAO) {
        this.httpUtils = httpUtils;
        this.iePlanDimensionSetDAO = iePlanDimensionSetDAO;
    }

    @Override
    public List<IEPlanDimensionSet> save(List<IEPlanDimensionSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanDimensionSetDAO.save(list);
        }else{
            log.error("IEPlanDimensionSet 数据为空！");
        }
        return list;
    }

    @Override
    public List<IEPlanDimensionSet> getAll() {
        return Lists.newArrayList(iePlanDimensionSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanDimensionSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanDimensionSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanDimensionSet+"?", null, HttpMethod.GET);
        List<IEPlanDimensionSet> list = OdataUtils.getResultsWithEntity(result, IEPlanDimensionSet.class);
        iePlanDimensionSetDAO.deleteAll();
        this.save(list);
        return list;
    }
}
