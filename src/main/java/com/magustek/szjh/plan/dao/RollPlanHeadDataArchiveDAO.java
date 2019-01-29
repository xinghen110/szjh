package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RollPlanHeadDataArchiveDAO extends CrudRepository<RollPlanHeadDataArchive, Long> {
    List<RollPlanHeadDataArchive> findAllByPlanHeadId(Long planHeadId);
    List<RollPlanHeadDataArchive> findAllByPlanHeadIdAndHtsno(Long planHeadId, String htsno);

    void deleteAllByPlanHeadId(Long planHeadId);

    List<RollPlanHeadDataArchive> findAllByPlanHeadIdAndDtvalContainsAndDmvalContainsAndZbart(Long planHeadId, String dtval, String dmval, String zbart);
    List<RollPlanHeadDataArchive> findAllByPlanHeadIdAndDtvalGreaterThanAndDmvalContainsAndZbart(Long planHeadId, String dtval, String dmval, String zbart);
    List<RollPlanHeadDataArchive> findAllByPlanHeadIdAndDtvalLessThanEqualAndDmvalContainsAndZbart(Long planHeadId, String dtval, String dmval, String zbart);
}
