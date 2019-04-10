package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanReleaseSet;
import com.magustek.szjh.configset.dao.IEPlanReleaseSetDAO;
import com.magustek.szjh.configset.service.IEPlanReleaseSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("IEPlanReleaseSetServiceImpl")
public class IEPlanReleaseSetServiceImpl implements IEPlanReleaseSetService {
    private final IEPlanReleaseSetDAO iePlanReleaseSetDAO;
    private final HttpUtils httpUtils;

    public IEPlanReleaseSetServiceImpl(IEPlanReleaseSetDAO iePlanReleaseSetDAO, HttpUtils httpUtils) {
        this.iePlanReleaseSetDAO = iePlanReleaseSetDAO;
        this.httpUtils = httpUtils;
    }


    @Override
    public List<IEPlanReleaseSet> save(List<IEPlanReleaseSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanReleaseSetDAO.save(list);
        }else{
            log.error("IEPlanReleaseSet 数据为空！");
        }
        return list;
    }

    @Override
    public List<IEPlanReleaseSet> getAll() {
        return Lists.newArrayList(iePlanReleaseSetDAO.findAll());
    }

    @Override
    public void deleteAll() {
        iePlanReleaseSetDAO.deleteAll();
    }
    @Override
    public List<IEPlanReleaseSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanReleaseSet+"?", null, HttpMethod.GET);
        List<IEPlanReleaseSet> list = OdataUtils.getListWithEntity(result, IEPlanReleaseSet.class);
        //清除现有数据
        deleteAll();
        //保存新数据
        save(list);
        return list;
    }

}
