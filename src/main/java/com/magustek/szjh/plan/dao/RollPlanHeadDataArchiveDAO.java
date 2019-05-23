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

    @SuppressWarnings("SqlResolve")
    @Query(value = "select head.id as head_id, " + //0
            "head.bukrs as head_bukrs, " +  //1
            "head.htsno as head_htsno, " +  //2
            "head.htnum as head_htnum, " +  //3
            "head.hdnum as head_hdnum, " +  //4
            "head.zbart as head_zbart, " +  //5
            "head.wears as head_wears, " +  //6
            "head.dtval as head_dtval, " +  //7
            "head.stval as head_stval, " +  //8
            "head.version as head_version, " +  //9
            "head.dmval as head_dmval, " +  //10
            "head.roll_id as head_roll_id, " +  //11
            "item.id as item_id, " +  //12
            "item.head_id as item_head_id, " +  //13
            "item.imnum as item_imnum, " +  //14
            "item.dtval as item_dtval, " +  //15
            "item.stval as item_stval, " +  //16
            "item.ctdtp as item_ctdtp, " +  //17
            "item.sdart as item_sdart, " +  //18
            "item.odue as item_odue, " +  //19
            "item.caval as item_caval " +  //20
            "from roll_plan_head_data_archive as head inner join roll_plan_item_data_archive as item on item.head_id=head.roll_id where head.plan_head_id=?1", nativeQuery = true)
    List<Object[]> findHeadWithItemByPlanHeadId(Long planHeadId);
}