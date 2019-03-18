package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.PlanItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlanItemDAO extends CrudRepository<PlanItem, Long> {
    void deleteAllByHeaderId(Long headerId);
    List<PlanItem> findAllByHeaderIdAndZbart(Long headerId, String zbart);
    List<PlanItem> findAllByHeaderIdAndDmval(Long headerId, String dmval);
    List<PlanItem> findAllByHeaderIdAndZtval(Long headerId, String ztval);

    List<PlanItem> findAllByHeaderId(Long headerId);

    @Query("select sum(zbval) , zbart from PlanItem where headerId=?1 and ztval <> 'T800' group by zbart")
    List<Object[]> arZbvalListByHeaderIdGroupByZbart(Long headerId);

    @Query("select sum(zbval) , zbart from PlanItem where headerId=?1 and ztval <> 'T800' and ztval like '% ' group by zbart")
    List<Object[]> mrZbvalListByHeaderIdGroupByZbart(Long headerId);
}
