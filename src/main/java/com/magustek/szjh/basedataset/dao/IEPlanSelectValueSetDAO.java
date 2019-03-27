package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IEPlanSelectValueSetDAO extends CrudRepository<IEPlanSelectValueSet, Long>{
    @Transactional
    void deleteAllByVersionAndReferencedIsNull(String version);
    void deleteAllByVersion(String version);
    List<IEPlanSelectValueSet> findAllByVersion(String version);
    List<IEPlanSelectValueSet> findAllByHtsnoAndVersion(String htsno, String version);
    List<IEPlanSelectValueSet> findAllByHtsnoInAndVersion(Set<String> htsnoSet, String version);

    List<IEPlanSelectValueSet> findAllByVersionAndPflag(String version, String pflag);

    @Transactional
    @Modifying
    @Query(value="UPDATE IEPlanSelectValueSet SET referenced= :referenced WHERE version= :version")
    int updateReferenced(@Param("referenced")String referenced,@Param("version")String version);

    @Query("from IEPlanSelectValueSet where version=?1 and " +
            "htsno in (select distinct(htsno) from IEPlanSelectValueSet where version=?1 and sdart=?2 and sdval is not null and sdval <> '') " +
            "and sdart in ?3")
    List<IEPlanSelectValueSet> findAllByVersionAndSdartInWithSerch(String version, String serch, List<String> sdartList);
}
