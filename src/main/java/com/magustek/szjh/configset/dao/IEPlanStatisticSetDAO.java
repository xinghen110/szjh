package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.IEPlanStatisticSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanStatisticSetDAO extends CrudRepository<IEPlanStatisticSet, Long> {
    List<IEPlanStatisticSet> findAllByBukrsAndRptypOrderByZsort(String bukrs, String rptyp);
}
