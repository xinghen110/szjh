package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.OrganizationSet;
import com.magustek.szjh.utils.KeyValueBean;

import java.util.List;
import java.util.Map;

public interface OrganizationSetService {
    List<OrganizationSet> save(List<OrganizationSet> list);
    List<OrganizationSet> getAll();
    void deleteAll();
    List<OrganizationSet> getAllFromDatasource() throws Exception;

    List<Object[]> getDpnumByBukrs(String bukrs) throws Exception;
    List<Object[]> getPonumByBukrs(String bukrs) throws Exception;
    List<Object[]> getUnameByBukrs(String bukrs) throws Exception;

    List<Object[]> getPonumByDpnum(String dpnum) throws Exception;
    List<Object[]> getUnameByDpnum(String dpnum) throws Exception;
    List<KeyValueBean> getRangeList() throws Exception;

    OrganizationSet getByBukrs(String bukrs);

    Map<String, String> orgKeyValue();

    Map<String, List<OrganizationSet>> getOrgMapByDmart(String dmart);
    void fillMap(Map<String, List<OrganizationSet>> orgMap, Map<String, Object> map, String dmart, String dmval);
}
