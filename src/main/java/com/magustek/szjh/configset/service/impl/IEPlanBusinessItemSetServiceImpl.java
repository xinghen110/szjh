package com.magustek.szjh.configset.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanBusinessItemSet;
import com.magustek.szjh.configset.bean.vo.IEPlanBusinessItemSetVO;
import com.magustek.szjh.configset.dao.IEPlanBusinessItemSetDAO;
import com.magustek.szjh.configset.service.IEPlanBusinessItemSetService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("IEPlanBusinessItemSetService")
public class IEPlanBusinessItemSetServiceImpl implements IEPlanBusinessItemSetService {
    private final HttpUtils httpUtils;
    private final IEPlanBusinessItemSetDAO iePlanBusinessItemSetDAO;

    public IEPlanBusinessItemSetServiceImpl(HttpUtils httpUtils, IEPlanBusinessItemSetDAO iePlanBusinessItemSetDAO) {
        this.httpUtils = httpUtils;
        this.iePlanBusinessItemSetDAO = iePlanBusinessItemSetDAO;
    }

    @Override
    public List<IEPlanBusinessItemSet> save(List<IEPlanBusinessItemSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanBusinessItemSetDAO.save(list);
        }else{
            log.error("IEPlanBusinessItemSet 数据为空！");
        }
        return list;
    }

    @Override
    public List<IEPlanBusinessItemSet> getAll() {
        return Lists.newArrayList(iePlanBusinessItemSetDAO.findAllByOrderByImnum());
    }

    @Override
    public List<IEPlanBusinessItemSetVO> getAllVO() {
        List<IEPlanBusinessItemSet> all = getAll();
        List<IEPlanBusinessItemSetVO> allVO = new ArrayList<>(all.size());
        all.forEach(item->{
            IEPlanBusinessItemSetVO vo = new IEPlanBusinessItemSetVO();
            BeanUtils.copyProperties(item, vo);
            allVO.add(vo);
        });
        return allVO;
    }

    @Override
    public Map<String, List<IEPlanBusinessItemSet>> getMap() {
        return getAll().stream().collect(Collectors.groupingBy(IEPlanBusinessItemSet::getImnum));
    }

    @Override
    public void deleteAll() {
        iePlanBusinessItemSetDAO.deleteAll();
    }

    @Override
    public List<IEPlanBusinessItemSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.IEPlanBusinessItemSet+"?", null, HttpMethod.GET);
        List<IEPlanBusinessItemSet> list = OdataUtils.getListWithEntity(result, IEPlanBusinessItemSet.class);
        //清除现有数据
        deleteAll();
        //保存新数据
        save(list);
        return list;
    }

    @Override
    public List<IEPlanBusinessItemSet> getAllByCaart(String caart) {
        return iePlanBusinessItemSetDAO.findAllByCaart(caart);
    }

    //获取imnum节点以及所有后续节点
    @Override
    public List<IEPlanBusinessItemSet> getNextItemList(String imnum){
        Map<String, List<IEPlanBusinessItemSet>> cache = getMap();
        List<IEPlanBusinessItemSet> list = new ArrayList<>();
        nextItem(cache, imnum, list);
        return list;
    }

    private void nextItem(Map<String, List<IEPlanBusinessItemSet>> cache, String imnum, List<IEPlanBusinessItemSet> list){
        IEPlanBusinessItemSet item = cache.get(imnum).get(0);
        list.add(item);
        if(!Strings.isNullOrEmpty(item.getNimnu())){
            nextItem(cache, item.getNimnu(), list);
        }
    }
}
