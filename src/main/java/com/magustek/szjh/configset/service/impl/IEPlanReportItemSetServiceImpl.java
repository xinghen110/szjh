package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanReportItemSet;
import com.magustek.szjh.configset.dao.IEPlanReportItemSetDAO;
import com.magustek.szjh.configset.service.IEPlanReportItemSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Slf4j
@Service("IEPlanReportItemSetService")
public class IEPlanReportItemSetServiceImpl implements IEPlanReportItemSetService {

    private final IEPlanReportItemSetDAO iePlanReportItemSetDAO;
    private final HttpUtils httpUtils;

    public IEPlanReportItemSetServiceImpl(IEPlanReportItemSetDAO iePlanReportItemSetDAO, HttpUtils httpUtils) {
        this.iePlanReportItemSetDAO = iePlanReportItemSetDAO;
        this.httpUtils = httpUtils;
    }

    @Override
    public List<IEPlanReportItemSet> save(List<IEPlanReportItemSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanReportItemSetDAO.save(list);
        }else{
            log.error("IEPlanOperationSet 数据为空！");
        }
        return list;
    }

    @Override
    public List<IEPlanReportItemSet> getAll() {
        return Lists.newArrayList(iePlanReportItemSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanReportItemSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanReportItemSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanReportItemSet+"?", null, HttpMethod.GET);
        List<IEPlanReportItemSet> list = OdataUtils.getListWithEntity(result, IEPlanReportItemSet.class);
        //清除现有数据
        deleteAll();
        //保存新数据
        save(list);
        return list;
    }

    @Override
    public List<IEPlanReportItemSet> getByBukrsAndRptyp(String bukrs, String rptyp) throws Exception {
        return iePlanReportItemSetDAO.findAllByBukrsAndRptypOrderByZsort(bukrs, rptyp);
    }

    @Override
    public List<IEPlanReportItemSet> getAllByIetyp() {
        return iePlanReportItemSetDAO.findAllByIetypIsNotNull();
    }
}
