package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.PlanLayout;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface PlanLayoutDAO extends CrudRepository<PlanLayout, Long> {
    PlanLayout findTopByHeaderId(Long headerId);
    @Transactional
    @Modifying
    void deleteAllByHeaderId(Long headerId);
}
