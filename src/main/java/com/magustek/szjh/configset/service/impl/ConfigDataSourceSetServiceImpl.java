package com.magustek.szjh.configset.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.ConfigDataSourceSet;
import com.magustek.szjh.configset.dao.ConfigDataSourceSetDAO;
import com.magustek.szjh.configset.service.ConfigDataSourceSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service("ConfigDataSourceSetService")
public class ConfigDataSourceSetServiceImpl implements ConfigDataSourceSetService {
    private final HttpUtils httpUtils;
    private final ConfigDataSourceSetDAO configDataSourceSetDAO;

    public ConfigDataSourceSetServiceImpl(HttpUtils httpUtils, ConfigDataSourceSetDAO configDataSourceSetDAO) {
        this.httpUtils = httpUtils;
        this.configDataSourceSetDAO = configDataSourceSetDAO;
    }


    @Override
    public List<ConfigDataSourceSet> fetchData() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.ConfigDataSource+"?", null, HttpMethod.GET);
        List<ConfigDataSourceSet> list = OdataUtils.getListWithEntity(result, ConfigDataSourceSet.class);
        configDataSourceSetDAO.deleteAll();
        configDataSourceSetDAO.save(list);
        log.info("数据源获取:"+JSON.toJSONString(list));
        return list;
    }

    @Override
    public List<ConfigDataSourceSet> getListByQcgrp(String qcgrp) throws Exception{
        List<ConfigDataSourceSet> list = configDataSourceSetDAO.findAllByQcgrp(qcgrp);
        log.info("数据源获取qcgrp="+qcgrp+":"+JSON.toJSONString(list));
        return list;
    }

    @Override
    public String getDescByQcgrpAndQcode(String qcgrp, String qcode) throws Exception {
        ConfigDataSourceSet config = configDataSourceSetDAO.findTopByQcgrpAndQcode(qcgrp, qcode);
        if(config == null){
            return "";
        }else{
            return config.getCotxt();
        }

    }
}
