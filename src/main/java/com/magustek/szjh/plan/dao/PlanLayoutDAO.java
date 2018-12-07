package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.PlanLayout;
import org.springframework.data.repository.CrudRepository;

public interface PlanLayoutDAO extends CrudRepository<PlanLayout, Long> {
    PlanLayout findTopByHeaderId(Long headerId);
}
