package com.magustek.szjh.basedataset.service.impl;

import com.magustek.szjh.basedataset.dao.IEPlanTermsSetDAO;
import com.magustek.szjh.basedataset.entity.IEPlanTermsSet;
import com.magustek.szjh.basedataset.service.IEPlanTermsSetService;
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
@Service("IEPlanTermsSetService")
public class IEPlanTermsSetServiceImpl implements IEPlanTermsSetService {

    private final HttpUtils httpUtils;
    private IEPlanTermsSetDAO iePlanTermsSetDAO;
    private OrganizationSetService organizationSetService;

    public IEPlanTermsSetServiceImpl(HttpUtils httpUtils, IEPlanTermsSetDAO iePlanTermsSetDAO, OrganizationSetService organizationSetService) {
        this.httpUtils = httpUtils;
        this.iePlanTermsSetDAO = iePlanTermsSetDAO;
        this.organizationSetService = organizationSetService;
    }

    @Override
    public List<IEPlanTermsSet> save(List<IEPlanTermsSet> list) {
        iePlanTermsSetDAO.save(list);
        return list;
    }

    @Override
    public List<IEPlanTermsSet> getAllByVersion(String version) {
        return iePlanTermsSetDAO.findAllByVersion(version);
    }

    @Override
    public void deleteAllByVersion(String version) {
        iePlanTermsSetDAO.deleteAllByVersion(version);
    }

    @Override
    public List<IEPlanTermsSet> fetchData() throws Exception {
        List<IEPlanTermsSet> list = new ArrayList<>();
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

    public List<IEPlanTermsSet> getAllFromDatasource(String bukrs) {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanTermsSet+"?", null, HttpMethod.GET);
        List<IEPlanTermsSet> list = null;
        try {
            list = OdataUtils.getListWithEntity(result, IEPlanTermsSet.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
