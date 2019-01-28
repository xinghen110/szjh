package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import com.magustek.szjh.basedataset.entity.RollPlanItemData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface RollPlanItemDataDAO extends CrudRepository<RollPlanItemData, Long> {
    @Transactional
    @Modifying
    void deleteAllByHeadIdIn(List<RollPlanHeadData> rollPlanHeadDataList);

    List<RollPlanItemData> findAllByHeadIdIn(List<RollPlanHeadData> headDataList);
}
