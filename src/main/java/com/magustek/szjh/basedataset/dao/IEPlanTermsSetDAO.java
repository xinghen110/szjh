package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.IEPlanTermsSet;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface IEPlanTermsSetDAO extends CrudRepository<IEPlanTermsSet, Long> {
    @Transactional
    void deleteAllByVersion(String version);
    List<IEPlanTermsSet> findAllByVersion(String version);
}
