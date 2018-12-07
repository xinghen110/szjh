package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.ConfigDataSourceSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfigDataSourceSetDAO extends CrudRepository<ConfigDataSourceSet, Long> {
    List<ConfigDataSourceSet> findAllByQcgrp(String qcgrp);
    ConfigDataSourceSet findTopByQcgrpAndQcode(String qcgrp, String qcode);
}
