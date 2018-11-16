package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanCalculationSet;
import com.magustek.szjh.configset.dao.IEPlanCalculationSetDAO;
import com.magustek.szjh.configset.service.IEPlanCalculationSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("IEPlanCalculationSetService")
public class IEPlanCalculationSetServiceImpl implements IEPlanCalculationSetService{
    private final IEPlanCalculationSetDAO iePlanCalculationSetDAO;
    private final HttpUtils httpUtils;

    public IEPlanCalculationSetServiceImpl(IEPlanCalculationSetDAO iePlanCalculationSetDAO, HttpUtils httpUtils) {
        this.iePlanCalculationSetDAO = iePlanCalculationSetDAO;
        this.httpUtils = httpUtils;
    }

    @Override
    public List<IEPlanCalculationSet> save(List<IEPlanCalculationSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanCalculationSetDAO.save(list);
        }else{
            log.error("IEPlanCalculationSet 数据为空！");
        }
        return list;
    }

    @Override
    public List<IEPlanCalculationSet> getAll() {
        return Lists.newArrayList(iePlanCalculationSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanCalculationSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanCalculationSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanCalculationSet+"?", null, HttpMethod.GET);
        List<IEPlanCalculationSet> list = OdataUtils.getResultsWithEntity(result, IEPlanCalculationSet.class);
        iePlanCalculationSetDAO.deleteAll();
        this.save(list);
        return list;
    }
}
