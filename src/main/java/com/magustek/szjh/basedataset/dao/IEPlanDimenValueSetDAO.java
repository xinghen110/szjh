package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanDimenValueSetDAO extends CrudRepository<IEPlanDimenValueSet, Long> {
    void deleteAllByVersion(String version);
    List<IEPlanDimenValueSet> findAllByVersion(String version);
    List<IEPlanDimenValueSet> findAllByHtsnoAndVersion(String htsno, String version);
    IEPlanDimenValueSet findTopByHtsnoAndVersionAndDmart(String htsno, String version, String dmart);
}
