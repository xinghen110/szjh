package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.IEPlanScreenItemSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanScreenItemSetDAO extends CrudRepository<IEPlanScreenItemSet, Long> {

    List<IEPlanScreenItemSet> findAllByHdnumOrderByZsort(String hdnum);

    IEPlanScreenItemSet findTopByImnum(String imnum);
}
