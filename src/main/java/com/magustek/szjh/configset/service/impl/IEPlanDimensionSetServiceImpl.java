package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanDimensionSet;
import com.magustek.szjh.configset.dao.IEPlanDimensionSetDAO;
import com.magustek.szjh.configset.service.IEPlanDimensionSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, IEPlanDimensionSet> getMappedList() {
        Map<String, IEPlanDimensionSet> map = new HashMap<>();
        getAll().forEach(i-> map.put(i.getDmart(), i));
        return map;
    }

    @Override
    public void deleteAll() {
        iePlanDimensionSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanDimensionSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanDimensionSet+"?", null, HttpMethod.GET);
        List<IEPlanDimensionSet> list = OdataUtils.getListWithEntity(result, IEPlanDimensionSet.class);
        //清除现有数据
        deleteAll();
        //保存新数据
        save(list);
        return list;
    }
}
