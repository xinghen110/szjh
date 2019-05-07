package com.magustek.szjh.configset.service.impl;

import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanBusinessHeadSet;
import com.magustek.szjh.configset.bean.vo.IEPlanBusinessHeadSetVO;
import com.magustek.szjh.configset.dao.IEPlanBusinessHeadSetDAO;
import com.magustek.szjh.configset.service.ConfigDataSourceSetService;
import com.magustek.szjh.configset.service.IEPlanBusinessHeadSetService;
import com.magustek.szjh.configset.service.IEPlanSelectDataSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("IEPlanBusinessHeadSetService")
public class IEPlanBusinessHeadSetServiceImpl implements IEPlanBusinessHeadSetService {
    private HttpUtils httpUtils;
    private IEPlanBusinessHeadSetDAO iePlanBusinessHeadSetDAO;
    private ConfigDataSourceSetService dataSourceSetService;

    public IEPlanBusinessHeadSetServiceImpl(HttpUtils httpUtils, IEPlanBusinessHeadSetDAO iePlanBusinessHeadSetDAO, ConfigDataSourceSetService dataSourceSetService) {
        this.httpUtils = httpUtils;
        this.iePlanBusinessHeadSetDAO = iePlanBusinessHeadSetDAO;
        this.dataSourceSetService = dataSourceSetService;
    }

    @Override
    public List<IEPlanBusinessHeadSet> save(List<IEPlanBusinessHeadSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanBusinessHeadSetDAO.save(list);
        }else{
            log.error("IEPlanBusinessHeadSet 数据为空！");
        }
        return list;
    }

    @Override
    public List<IEPlanBusinessHeadSet> getAll() {
        return Lists.newArrayList(iePlanBusinessHeadSetDAO.findAll());
    }

    @Override
    public List<IEPlanBusinessHeadSet> getAllByBukrsAndRptyp(String bukrs, String rptyp) {
        return iePlanBusinessHeadSetDAO.findAllByBukrsAndRptypOrderByHdnum(bukrs, rptyp);
    }

    @Override
    public List<IEPlanBusinessHeadSetVO> getAllVOByBukrsAndRptyp(String bukrs, String rptyp) {
        List<IEPlanBusinessHeadSet> list = getAllByBukrsAndRptyp(bukrs, rptyp);
        List<IEPlanBusinessHeadSetVO> voList = new ArrayList<>(list.size());
        list.forEach(h->{
            IEPlanBusinessHeadSetVO vo = new IEPlanBusinessHeadSetVO(dataSourceSetService);
            BeanUtils.copyProperties(h, vo);
            voList.add(vo);
        });
        return voList;
    }

    @Override
    public void deleteAll() {
        iePlanBusinessHeadSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanBusinessHeadSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanBusinessHeadSet+"?", null, HttpMethod.GET);
        List<IEPlanBusinessHeadSet> list = OdataUtils.getListWithEntity(result, IEPlanBusinessHeadSet.class);
        //清除现有数据
        deleteAll();
        //保存新数据
        save(list);
        return list;
    }
}
