package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.IEPlanPaymentSet;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface IEPlanPaymentSetDAO extends CrudRepository<IEPlanPaymentSet, Long> {
    List<IEPlanPaymentSet> findAllByVersion(String version);
    @Transactional
    void deleteAllByVersion(String version);
    @Transactional
    void deleteAllByVersionAndBukrs(String version, String bukrs);
}
