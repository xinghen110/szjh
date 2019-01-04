package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.IEPlanBusinessHeadSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanBusinessHeadSetDAO extends CrudRepository<IEPlanBusinessHeadSet, Long> {
    List<IEPlanBusinessHeadSet> findAllByBukrsAndRptyp(String bukrs, String rptyp);
}
