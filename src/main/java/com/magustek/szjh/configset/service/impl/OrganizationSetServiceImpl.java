package com.magustek.szjh.configset.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.magustek.szjh.configset.bean.IEPlanDimensionSet;
import com.magustek.szjh.configset.bean.OrganizationSet;
import com.magustek.szjh.configset.dao.OrganizationSetDAO;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.user.bean.CompanyModel;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.KeyValueBean;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.constant.RedisKeys;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("OrganizationSetService")
public class OrganizationSetServiceImpl implements OrganizationSetService {
    private final HttpUtils httpUtils;
    private final OrganizationSetDAO organizationSetDAO;
    private RedisTemplate<String, Object> redisTemplate;

    public OrganizationSetServiceImpl(HttpUtils httpUtils, OrganizationSetDAO organizationSetDAO, RedisTemplate<String, Object> redisTemplate) {
        this.httpUtils = httpUtils;
        this.organizationSetDAO = organizationSetDAO;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<OrganizationSet> save(List<OrganizationSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0){
            organizationSetDAO.save(list);
        }else{
            log.error("IEPlanSelectDataSet 数据为空！");
        }
        organizationSetDAO.save(list);
        return list;
    }

    @Override
    public List<OrganizationSet> getAll() {
        return Lists.newArrayList(organizationSetDAO.findAll());
    }

    @Transactional
    @Override
    public void deleteAll() {
        organizationSetDAO.deleteAll();
    }

    @Override
    public List<OrganizationSet> getAllFromDatasource() throws Exception {
        String result = httpUtils.getResultByUrl(OdataUtils.OrginazationSet+"?", null, HttpMethod.GET);
        List<OrganizationSet> list = OdataUtils.getListWithEntity(result, OrganizationSet.class);
        //清除现有数据
        deleteAll();
        //保存新数据
        save(list);
        //初始化缓存
        orgKeyValue();
        return list;
    }

    @Override
    public List<Object[]> getDpnumByBukrs(String bukrs){
        return organizationSetDAO.findDistinctDpnumByBukrs(bukrs);
    }

    @Override
    public List<Object[]> getPonumByBukrs(String bukrs){
        return organizationSetDAO.findDistinctPonumByBukrs(bukrs);
    }

    @Override
    public List<Object[]> getUnameByBukrs(String bukrs){
        return organizationSetDAO.findDistinctUnameByBukrs(bukrs);
    }

    @Override
    public List<Object[]> getPonumByDpnum(String dpnum){
        return organizationSetDAO.findDistinctPonumByDpnum(dpnum);
    }

    @Override
    public List<Object[]> getUnameByDpnum(String dpnum){
        return organizationSetDAO.findDistinctUnameByDpnum(dpnum);
    }

    /**
     * 获取需要取数的公司，及其起始、截止日期
     * key : bukrs
     * value: begin(yyyy-MM-dd)
     * opera: end(yyyy-MM-dd)
     * */
    @Override
    public List<KeyValueBean> getRangeList(){
        List<Object[]> bukrsList = organizationSetDAO.findDistinctBukrs();
        List<KeyValueBean> list = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for(Object[] o : bukrsList){
            KeyValueBean bean = new KeyValueBean();
            LocalDate his = ClassUtils.getDate(now, o[1].toString(), Integer.parseInt(o[2].toString()), false);
            bean.put(o[0].toString(), his.toString(), now.toString());
            list.add(bean);
        }
        log.info("待取数公司，及其时间范围："+JSON.toJSONString(list));
        return list;
    }

    @Override
    public OrganizationSet getByBukrs(String bukrs){
        return organizationSetDAO.findTopByBukrsOrderByCsortAsc(bukrs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> orgKeyValue() {
        Map<String, String> object = (Map<String, String>)redisTemplate.opsForValue().get(RedisKeys.ORG_MAP);
        Map<String, String> map;
        if(ClassUtils.isEmpty(object)){
            List<OrganizationSet> bukrs = organizationSetDAO.findDistinctBukrsByOrderByCsort();
            List<OrganizationSet> dpnum = organizationSetDAO.findDistinctDpnumByOrderByDsort();
            List<OrganizationSet> ponum = organizationSetDAO.findDistinctPonumByOrderByDsort();
            List<OrganizationSet> uname = organizationSetDAO.findDistinctUnameByOrderByDsort();
            map = new HashMap<>(bukrs.size()+dpnum.size()+ponum.size()+uname.size());
            bukrs.forEach(o-> map.put(o.getBukrs(), o.getButxt()));
            dpnum.forEach(o-> map.put(o.getDpnum(), o.getDpnam()));
            ponum.forEach(o-> map.put(o.getPonum(), o.getPonam()));
            uname.forEach(o-> map.put(o.getUname(), o.getUsnam()));
            redisTemplate.opsForValue().set(RedisKeys.ORG_MAP, map);
        }else{
            map = object;
        }

        return map;
    }

    @Override
    public Map<String, List<OrganizationSet>> getOrgMapByDmart(String dmart) {
        Map<String, List<OrganizationSet>> orgMap;
        switch (dmart){
            case "D100":
                orgMap = getAll().stream().collect(Collectors.groupingBy(OrganizationSet::getBukrs));
                break;
            case "D110":
                orgMap = getAll().stream().collect(Collectors.groupingBy(OrganizationSet::getDpnum));
                break;
            case "D120":
                orgMap = getAll().stream().collect(Collectors.groupingBy(OrganizationSet::getUname));
                break;
            default:
                return null;
        }
        return orgMap;
    }

    @Override
    public void fillMap(Map<String, List<OrganizationSet>> orgMap, Map<String, Object> map, String dmart, String dmval) {
        //map.put("dmart", dmart);
        switch (dmart){
            case "D100":
                map.put("dmval",orgMap.get(dmval).get(0).getBukrs());
                map.put("dmtxt",orgMap.get(dmval).get(0).getButxt());
                map.put("sort",orgMap.get(dmval).get(0).getCsort());
                break;
            case "D110":
                map.put("dmval",orgMap.get(dmval).get(0).getDpnum());
                map.put("dmtxt",orgMap.get(dmval).get(0).getDpnam());
                map.put("sort",orgMap.get(dmval).get(0).getDsort());
                break;
            case "D120":
                map.put("dmval",orgMap.get(dmval).get(0).getUname());
                map.put("dmtxt",orgMap.get(dmval).get(0).getUsnam());
                map.put("sort",orgMap.get(dmval).get(0).getDsort());
                break;
        }
    }

    @Override
    //返回指定组织机构树
    public ArrayList<KeyValueBean> getORG(String voBukrs, String voDmart, String dmart) throws Exception {
        ArrayList<KeyValueBean> keyValueBeans = new ArrayList<>();
        List<Object[]> list;
        KeyValueBean bean;
        CompanyModel company = ContextUtils.getCompany();
        switch (voDmart){
            case IEPlanDimensionSet.DM_Company:
                OrganizationSet org = getByBukrs(voBukrs);
                bean = new KeyValueBean();
                bean.put(org.getBukrs(), org.getButxt());
                keyValueBeans.add(bean);
                break;
            case IEPlanDimensionSet.DM_Department:
                //如果是编制部门计划，就取当前用户所在部门
                if(IEPlanDimensionSet.DM_Department.equals(dmart)){
                    bean = new KeyValueBean();
                    bean.put(company.getDeptcode(), company.getGtext());
                    keyValueBeans.add(bean);
                }else{
                    list = getDpnumByBukrs(voBukrs);
                    for(Object[] o : list) {
                        bean = new KeyValueBean();
                        bean.put((String)o[0], (String)o[1]);
                        keyValueBeans.add(bean);
                    }
                }
                break;
            case IEPlanDimensionSet.DM_User:
                //如果是编制部门计划，就取当前用户所在部门的用户
                if(IEPlanDimensionSet.DM_Department.equals(dmart)){
                    list = getUnameByDpnum(company.getDeptcode());
                }else {
                    list = getUnameByBukrs(voBukrs);
                }
                for(Object[] o : list) {
                    bean = new KeyValueBean();
                    bean.put((String)o[0], (String)o[1]);
                    keyValueBeans.add(bean);
                }
                break;
            default :
                log.error("axis error:" + voDmart);
                throw new Exception("axis error:" + voDmart);
        }
        return keyValueBeans;
    }

    @Override
    public OrganizationSet getApprover(String bukrs, String uname) {
        return organizationSetDAO.findByBukrsAndUname(bukrs, uname);
    }
}
