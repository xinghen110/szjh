package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface IEPlanSelectValueSetDAO extends CrudRepository<IEPlanSelectValueSet, Long>{
    @Transactional
    void deleteAllByVersionAndReferencedIsNull(String version);
    List<IEPlanSelectValueSet> findAllByVersion(String version);
    List<IEPlanSelectValueSet> findAllByHtsnoAndVersion(String htsno, String version);

    List<IEPlanSelectValueSet> findAllByVersionAndPflag(String version, String pflag);

    @Transactional
    @Modifying
    @Query(value="UPDATE IEPlanSelectValueSet SET referenced= :referenced WHERE version= :version")
    int updateReferenced(@Param("referenced")String referenced,@Param("version")String version);
}
