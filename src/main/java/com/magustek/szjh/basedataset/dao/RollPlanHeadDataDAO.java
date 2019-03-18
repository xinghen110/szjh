package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface RollPlanHeadDataDAO extends CrudRepository<RollPlanHeadData, Long> {
    @Transactional
    @Modifying
    @Query("delete from RollPlanHeadData where version=?1")
    void deleteAllByVersion(String version);

    //@Query(value="select * from roll_plan_head_data WHERE version= :version and bukrs= :bukrs", nativeQuery = true)
    List<RollPlanHeadData> findAllByVersionAndBukrs(String version, String bukrs);

    //@Query(value="select * from roll_plan_head_data WHERE version= :version and htsno= :htsno", nativeQuery = true)
    List<RollPlanHeadData> findAllByVersionAndHtsno(String version, String htsno);

    List<RollPlanHeadData> findAllByVersionAndDtvalIsNotNullAndDtvalIsNotAndZbart(String version, String not, String zbart);
}
