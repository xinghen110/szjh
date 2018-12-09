package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanSelectValueSetDAO extends CrudRepository<IEPlanSelectValueSet, Long>{
    void deleteAllByVersion(String version);
    List<IEPlanSelectValueSet> findAllByVersion(String version);
}
