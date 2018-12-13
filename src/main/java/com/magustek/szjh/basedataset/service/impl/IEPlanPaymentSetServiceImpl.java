package com.magustek.szjh.basedataset.service.impl;

import com.magustek.szjh.basedataset.dao.IEPlanPaymentSetDAO;
import com.magustek.szjh.basedataset.entity.IEPlanPaymentSet;
import com.magustek.szjh.basedataset.service.IEPlanPaymentSetService;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.utils.KeyValueBean;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("IEPlanPaymentSetService")
public class IEPlanPaymentSetServiceImpl implements IEPlanPaymentSetService {
    private final HttpUtils httpUtils;
    private IEPlanPaymentSetDAO iePlanPaymentSetDAO;
    private OrganizationSetService organizationSetService;

    public IEPlanPaymentSetServiceImpl(HttpUtils httpUtils, IEPlanPaymentSetDAO iePlanPaymentSetDAO, OrganizationSetService organizationSetService) {
        this.httpUtils = httpUtils;
        this.iePlanPaymentSetDAO = iePlanPaymentSetDAO;
        this.organizationSetService = organizationSetService;
    }

    @Override
    public List<IEPlanPaymentSet> save(List<IEPlanPaymentSet> list) {
        iePlanPaymentSetDAO.save(list);
        return list;
    }

    @Override
    public List<IEPlanPaymentSet> getAllByVersion(String version) {
        return iePlanPaymentSetDAO.findAllByVersion(version);
    }

    @Override
    public void deleteAllByVersion(String version) {
        iePlanPaymentSetDAO.deleteAllByVersion(version);
    }

    @Override
    public void deleteAllByVersionAndBukrs(String version, String bukrs) {
        iePlanPaymentSetDAO.deleteAllByVersionAndBukrs(version,bukrs);
    }

    @Override
    public List<IEPlanPaymentSet> fetchData() throws Exception {
        List<IEPlanPaymentSet> list = new ArrayList<>();
        String version = LocalDate.now().toString();
        List<KeyValueBean> bukrsList = organizationSetService.getRangeList();

        bukrsList.forEach(item->list.addAll(getAllFromDatasource(item.getKey())));

        list.forEach(i->i.setVersion(version));
        //清除今天的版本
        deleteAllByVersion(version);
        //保存新的今天版本
        save(list);
        return list;
    }

    public List<IEPlanPaymentSet> getAllFromDatasource(String bukrs) {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanPaymentSet+"?$filter=bukrs eq '"+bukrs+"'", null, HttpMethod.GET);
        List<IEPlanPaymentSet> list = null;
        try {
            list = OdataUtils.getListWithEntity(result, IEPlanPaymentSet.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
