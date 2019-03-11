package com.magustek.szjh.configset.service.impl;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.config.RedisConfig;
import com.magustek.szjh.configset.bean.ConfigDataSourceSet;
import com.magustek.szjh.configset.dao.ConfigDataSourceSetDAO;
import com.magustek.szjh.configset.service.ConfigDataSourceSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("ConfigDataSourceSetService")
public class ConfigDataSourceSetServiceImpl implements ConfigDataSourceSetService {
    private final HttpUtils httpUtils;
    private final ConfigDataSourceSetDAO configDataSourceSetDAO;
    private RedisTemplate<String, Object> redisTemplate;

    public ConfigDataSourceSetServiceImpl(HttpUtils httpUtils, ConfigDataSourceSetDAO configDataSourceSetDAO, RedisTemplate<String, Object> redisTemplate) {
        this.httpUtils = httpUtils;
        this.configDataSourceSetDAO = configDataSourceSetDAO;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public List<ConfigDataSourceSet> fetchData() throws Exception {
        //清空缓存
        redisTemplate.opsForValue().set(RedisConfig.ConfigDataSourceSet, null);
        String result = httpUtils.getResultByUrl(OdataUtils.ConfigDataSource+"?", null, HttpMethod.GET);
        List<ConfigDataSourceSet> list = OdataUtils.getListWithEntity(result, ConfigDataSourceSet.class);
        configDataSourceSetDAO.deleteAll();
        configDataSourceSetDAO.save(list);
        setRedis(list.iterator());
        log.info("数据源获取:"+JSON.toJSONString(list));
        return list;
    }

    @Override
    public List<ConfigDataSourceSet> getListByQcgrp(String qcgrp){
        List<ConfigDataSourceSet> list = configDataSourceSetDAO.findAllByQcgrp(qcgrp);
        log.info("数据源获取qcgrp="+qcgrp+":"+JSON.toJSONString(list));
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getDescByQcgrpAndQcode(String qcgrp, String qcode) {
        Object object = redisTemplate.opsForValue().get(RedisConfig.ConfigDataSourceSet);
        Map<String, String> map;
        if(object == null){
            Iterator<ConfigDataSourceSet> all = configDataSourceSetDAO.findAll().iterator();
            map = setRedis(all);
        }else{
            map = (Map<String, String>)object;
        }
        return map.get(qcgrp+"-"+qcode);
    }

    private Map<String, String> setRedis(Iterator<ConfigDataSourceSet> all){
        Map<String, String> map = new HashMap<>();
        while (all.hasNext()){
            ConfigDataSourceSet next = all.next();
            map.put(next.getQcgrp()+"-"+next.getQcode(), next.getCotxt());
        }
        redisTemplate.opsForValue().set(RedisConfig.ConfigDataSourceSet, map);
        return map;
    }
}
