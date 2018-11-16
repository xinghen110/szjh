package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanOperationSet;
import com.magustek.szjh.configset.dao.IEPlanOperationSetDAO;
import com.magustek.szjh.configset.service.IEPlanOperationSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("IEPlanOperationSetService")
public class IEPlanOperationSetServiceImpl implements IEPlanOperationSetService {
    private final IEPlanOperationSetDAO iePlanOperationSetDAO;
    private final HttpUtils httpUtils;

    @Autowired
    public IEPlanOperationSetServiceImpl(IEPlanOperationSetDAO iePlanOperationSetDAO, HttpUtils httpUtils) {
        this.iePlanOperationSetDAO = iePlanOperationSetDAO;
        this.httpUtils = httpUtils;
    }

    @Override
    public List<IEPlanOperationSet> save(List<IEPlanOperationSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanOperationSetDAO.save(list);
        }else{
            log.error("IEPlanOperationSet 数据为空！");
        }
        return list;
    }

    @Override
    public List<IEPlanOperationSet> getAll() {
        return Lists.newArrayList(iePlanOperationSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanOperationSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanOperationSet> getAllFromDatasource() throws Exception{
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanOperationSet+"?", null, HttpMethod.GET);
        List<IEPlanOperationSet> list = OdataUtils.getResultsWithEntity(result, IEPlanOperationSet.class);
        iePlanOperationSetDAO.deleteAll();
        this.save(list);
        return list;
    }
}
