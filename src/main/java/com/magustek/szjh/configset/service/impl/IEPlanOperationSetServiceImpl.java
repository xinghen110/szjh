package com.magustek.szjh.configset.service.impl;

import com.magustek.szjh.configset.bean.IEPlanOperationSet;
import com.magustek.szjh.configset.dao.IEPlanOperationSetDAO;
import com.magustek.szjh.configset.service.IEPlanOperationSetService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.constant.RedisKeys;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("IEPlanOperationSetService")
public class IEPlanOperationSetServiceImpl implements IEPlanOperationSetService {
    private final IEPlanOperationSetDAO iePlanOperationSetDAO;
    private final HttpUtils httpUtils;
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public IEPlanOperationSetServiceImpl(IEPlanOperationSetDAO iePlanOperationSetDAO, HttpUtils httpUtils, RedisTemplate<String, Object> redisTemplate) {
        this.iePlanOperationSetDAO = iePlanOperationSetDAO;
        this.httpUtils = httpUtils;
        this.redisTemplate = redisTemplate;
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
        return iePlanOperationSetDAO.findAllByOrderByZsortAsc();
    }

    @Override
    public void deleteAll() {
        iePlanOperationSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanOperationSet> getAllFromDatasource() throws Exception{
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanOperationSet+"?", null, HttpMethod.GET);
        List<IEPlanOperationSet> list = OdataUtils.getListWithEntity(result, IEPlanOperationSet.class);
        //清除现有数据
        flushZbnamMapCache();
        deleteAll();
        //保存新数据
        save(list);
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> getZbnamMap(){
        Object object = redisTemplate.opsForValue().get(RedisKeys.ZB_MAP);
        Map<String, String> map;
        if(object == null){
            Iterator<IEPlanOperationSet> all = iePlanOperationSetDAO.findAll().iterator();
            map = new HashMap<>();
            while (all.hasNext()){
                IEPlanOperationSet next = all.next();
                map.put(next.getZbart(), next.getZbnam());
            }
            redisTemplate.opsForValue().set(RedisKeys.ZB_MAP, map);
        }else{
            map = (Map<String, String>)object;
        }

        return map;
    }

    private void flushZbnamMapCache(){
        redisTemplate.delete(RedisKeys.ZB_MAP);
    }
}
