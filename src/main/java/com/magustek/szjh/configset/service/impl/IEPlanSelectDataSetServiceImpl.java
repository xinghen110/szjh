package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanSelectDataSet;
import com.magustek.szjh.configset.dao.IEPlanSelectDataSetDAO;
import com.magustek.szjh.configset.service.IEPlanSelectDataSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service("IEPlanSelectDataSetService")
public class IEPlanSelectDataSetServiceImpl implements IEPlanSelectDataSetService {
    private final IEPlanSelectDataSetDAO iePlanSelectDataSetDAO;
    private final HttpUtils httpUtils;

    public IEPlanSelectDataSetServiceImpl(IEPlanSelectDataSetDAO iePlanSelectDataSetDAO, HttpUtils httpUtils) {
        this.iePlanSelectDataSetDAO = iePlanSelectDataSetDAO;
        this.httpUtils = httpUtils;
    }

    @Override
    public List<IEPlanSelectDataSet> save(List<IEPlanSelectDataSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0){
            iePlanSelectDataSetDAO.save(list);
        }else{
            log.error("IEPlanSelectDataSet 数据为空！");
        }
        iePlanSelectDataSetDAO.save(list);
        return list;
    }

    @Override
    public List<IEPlanSelectDataSet> getAll() {
        return Lists.newArrayList(iePlanSelectDataSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanSelectDataSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanSelectDataSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanSelectDataSet+"?", null, HttpMethod.GET);
        List<IEPlanSelectDataSet> list = OdataUtils.getListWithEntity(result, IEPlanSelectDataSet.class);
        iePlanSelectDataSetDAO.deleteAll();
        this.save(list);
        return list;
    }
}
