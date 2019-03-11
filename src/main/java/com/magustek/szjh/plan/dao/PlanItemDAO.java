package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.PlanItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlanItemDAO extends CrudRepository<PlanItem, Long> {
    void deleteAllByHeaderId(Long headerId);
    List<PlanItem> findAllByHeaderIdAndAndZbart(Long headerId, String zbart);
    List<PlanItem> findAllByHeaderIdAndAndDmval(Long headerId, String dmval);
    List<PlanItem> findAllByHeaderIdAndAndZtval(Long headerId, String ztval);

    List<PlanItem> findAllByHeaderId(Long headerId);
}
