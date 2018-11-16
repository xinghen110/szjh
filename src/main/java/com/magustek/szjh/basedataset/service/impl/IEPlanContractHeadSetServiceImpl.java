package com.magustek.szjh.basedataset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.basedataset.dao.IEPlanContractHeadSetDAO;
import com.magustek.szjh.basedataset.entity.IEPlanContractHeadSet;
import com.magustek.szjh.basedataset.service.IEPlanContractHeadSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("IEPlanContractHeadSetService")
public class IEPlanContractHeadSetServiceImpl implements IEPlanContractHeadSetService {
    private final HttpUtils httpUtils;
    private final IEPlanContractHeadSetDAO iePlanContractHeadSetDAO;

    public IEPlanContractHeadSetServiceImpl(HttpUtils httpUtils, IEPlanContractHeadSetDAO iePlanContractHeadSetDAO) {
        this.httpUtils = httpUtils;
        this.iePlanContractHeadSetDAO = iePlanContractHeadSetDAO;
    }

    @Override
    public List<IEPlanContractHeadSet> save(List<IEPlanContractHeadSet> list) {
        iePlanContractHeadSetDAO.save(list);
        return list;
    }

    @Override
    public List<IEPlanContractHeadSet> getAll() {
        return Lists.newArrayList(iePlanContractHeadSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanContractHeadSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanContractHeadSet> getAllFromDatasource(String begin, String end) throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanOperationSet+"?$filter=chdate ge '"+begin+"' and chdate le '"+end+"'", null, HttpMethod.GET);
        List<IEPlanContractHeadSet> list = OdataUtils.getResultsWithEntity(result, IEPlanContractHeadSet.class);
        iePlanContractHeadSetDAO.deleteAll();
        iePlanContractHeadSetDAO.save(list);
        return list;
    }
}
