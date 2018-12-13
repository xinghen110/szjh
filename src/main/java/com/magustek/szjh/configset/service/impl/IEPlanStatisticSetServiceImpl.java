package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanStatisticSet;
import com.magustek.szjh.configset.dao.IEPlanStatisticSetDAO;
import com.magustek.szjh.configset.service.IEPlanStatisticSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("IEPlanStatisticSetService")
public class IEPlanStatisticSetServiceImpl implements IEPlanStatisticSetService {
    private final HttpUtils httpUtils;
    private IEPlanStatisticSetDAO iePlanStatisticSetDAO;

    public IEPlanStatisticSetServiceImpl(HttpUtils httpUtils, IEPlanStatisticSetDAO iePlanStatisticSetDAO) {
        this.httpUtils = httpUtils;
        this.iePlanStatisticSetDAO = iePlanStatisticSetDAO;
    }

    @Override
    public List<IEPlanStatisticSet> save(List<IEPlanStatisticSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0){
            iePlanStatisticSetDAO.save(list);
        }else{
            log.error("IEPlanStatisticSet 数据为空！");
        }
        iePlanStatisticSetDAO.save(list);
        return list;
    }

    @Override
    public List<IEPlanStatisticSet> getAll() {
        return Lists.newArrayList(iePlanStatisticSetDAO.findAll());
    }

    @Override
    public List<IEPlanStatisticSet> getAllByBukrsAndRptyp(String bukrs, String rptyp) {
        return iePlanStatisticSetDAO.findAllByBukrsAndRptypOrderByZsort(bukrs, rptyp);
    }

    @Override
    public void deleteAll() {
        iePlanStatisticSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanStatisticSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanStatisticSet+"?", null, HttpMethod.GET);
        List<IEPlanStatisticSet> list = OdataUtils.getListWithEntity(result, IEPlanStatisticSet.class);
        //清除现有数据
        deleteAll();
        //保存新数据
        save(list);
        return list;
    }
}
