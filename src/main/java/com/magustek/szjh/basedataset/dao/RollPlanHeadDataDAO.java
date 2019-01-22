package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface RollPlanHeadDataDAO extends CrudRepository<RollPlanHeadData, Long> {
    @Transactional
    @Modifying
    void deleteAllByVersion(String version);
    List<RollPlanHeadData> findAllByVersionAndBukrs(String version, String bukrs);
    List<RollPlanHeadData> findAllByVersionAndHtsno(String version, String htsno);
}
