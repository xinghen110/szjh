package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface IEPlanSelectValueSetDAO extends CrudRepository<IEPlanSelectValueSet, Long>{
    @Transactional
    void deleteAllByVersion(String version);
    List<IEPlanSelectValueSet> findAllByVersion(String version);
    List<IEPlanSelectValueSet> findAllByHtsnoAndVersion(String htsno, String version);

    List<IEPlanSelectValueSet> findAllByVersionAndSdartInAndPflag(String version, Collection<String> sdart, String pflag);
}
