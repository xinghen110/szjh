package com.magustek.szjh.configset.dao;

import org.springframework.data.repository.CrudRepository;
import com.magustek.szjh.configset.bean.IEPlanOperationSet;

import java.util.List;

public interface IEPlanOperationSetDAO extends CrudRepository<IEPlanOperationSet, Long> {
    IEPlanOperationSet findByZbart(String zbart);
    List<IEPlanOperationSet> findAllByOrderByZsortAsc();
}
