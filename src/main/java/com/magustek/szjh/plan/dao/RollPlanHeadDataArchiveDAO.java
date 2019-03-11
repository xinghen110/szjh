package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RollPlanHeadDataArchiveDAO extends CrudRepository<RollPlanHeadDataArchive, Long> {
    List<RollPlanHeadDataArchive> findAllByPlanHeadId(Long planHeadId);
    List<RollPlanHeadDataArchive> findAllByPlanHeadIdAndHtsno(Long planHeadId, String htsno);

    @Modifying
    @Query("delete from RollPlanHeadDataArchive where planHeadId=?1")
    void deleteAllByPlanHeadId(Long planHeadId);

    List<RollPlanHeadDataArchive> findAllByPlanHeadIdAndDtvalContainsAndDmvalContainsAndZbart(Long planHeadId, String dtval, String dmval, String zbart);
    List<RollPlanHeadDataArchive> findAllByPlanHeadIdAndDtvalGreaterThanAndDmvalContainsAndZbart(Long planHeadId, String dtval, String dmval, String zbart);
    List<RollPlanHeadDataArchive> findAllByPlanHeadIdAndDtvalLessThanEqualAndDmvalContainsAndZbart(Long planHeadId, String dtval, String dmval, String zbart);

    List<RollPlanHeadDataArchive> findAllByPlanHeadIdAndDmvalContainsAndZbart(Long planHeadId, String dmval, String zbart);

    @Query(value = "select sum(wears) , zbart from RollPlanHeadDataArchive " +
            "where planHeadId=?1 and dtval < ?2 and dtval is not null and dtval <> '' group by zbart")
    List<Object[]> zbvalListByPlanHeadIdGroupByZbart(Long planHeadId, String dtval);
}