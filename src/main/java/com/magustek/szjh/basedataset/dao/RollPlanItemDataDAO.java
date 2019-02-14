package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import com.magustek.szjh.basedataset.entity.RollPlanItemData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface RollPlanItemDataDAO extends CrudRepository<RollPlanItemData, Long> {
    @Transactional
    @Modifying
    void deleteAllByHeadIdIn(List<RollPlanHeadData> rollPlanHeadDataList);

    @Transactional
    @Modifying
    @Query(value = "delete from roll_plan_item_data where head_Id in (select id from roll_plan_head_data where version=:version)" , nativeQuery = true)
    void deleteByVersion(@Param("version")String version);

    List<RollPlanItemData> findAllByHeadIdIn(List<RollPlanHeadData> headDataList);
}
