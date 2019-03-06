package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.RollPlanItemDataArchive;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RollPlanItemDataArchiveDAO extends CrudRepository<RollPlanItemDataArchive, Long> {
    List<RollPlanItemDataArchive> findAllByPlanHeadId(Long planHeadId);
    List<RollPlanItemDataArchive> findAllByHeadId(Long rollPlanHeadId);
    @Modifying
    @Query("delete from RollPlanItemDataArchive where planHeadId=?1")
    void deleteAllByPlanHeadId(Long planHeadId);

    List<RollPlanItemDataArchive> findAllByHeadIdInAndImnumIn(List<Long> headIdList, List<String> imnumList);
}
