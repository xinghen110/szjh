package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RollPlanHeadDataArchiveDAO extends CrudRepository<RollPlanHeadDataArchive, Long> {
    List<RollPlanHeadDataArchive> findAllByPlanHeadId(Long planHeadId);

    void deleteAllByPlanHeadId(Long planHeadId);
}
