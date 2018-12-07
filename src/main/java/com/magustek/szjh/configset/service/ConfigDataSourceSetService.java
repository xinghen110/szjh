package com.magustek.szjh.configset.service;

import com.magustek.szjh.configset.bean.ConfigDataSourceSet;

import java.util.List;

public interface ConfigDataSourceSetService {
    List<ConfigDataSourceSet> fetchData() throws Exception;
    List<ConfigDataSourceSet> getListByQcgrp(String qcgrp) throws Exception;
    String getDescByQcgrpAndQcode(String qcgrp, String qcode) throws Exception;
}
