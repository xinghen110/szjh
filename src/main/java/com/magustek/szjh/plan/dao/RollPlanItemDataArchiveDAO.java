package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.RollPlanItemDataArchive;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RollPlanItemDataArchiveDAO extends CrudRepository<RollPlanItemDataArchive, Long> {
    List<RollPlanItemDataArchive> findAllByPlanHeadId(Long planHeadId);
    List<RollPlanItemDataArchive> findAllByHeadId(Long rollPlanHeadId);

    void deleteAllByPlanHeadId(Long planHeadId);
}
