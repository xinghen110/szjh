package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.IEPlanBusinessItemSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanBusinessItemSetDAO extends CrudRepository<IEPlanBusinessItemSet, Long> {
    List<IEPlanBusinessItemSet> findAllByOrderByImnum();
}
