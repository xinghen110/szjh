package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.PlanHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


/**
 * @author hexin
 */
public interface PlanHeaderDAO extends CrudRepository<PlanHeader, Long> {
    Page<PlanHeader> findAllByBukrsAndOrgvalAndRptypOrderByIdDesc(String bukrs, String orgval, String rptyp, Pageable pageable);

    PlanHeader findTopByRptypOrderByCkdateDesc(String rptyp);
}
