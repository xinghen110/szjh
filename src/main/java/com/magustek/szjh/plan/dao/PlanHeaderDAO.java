package com.magustek.szjh.plan.dao;

import com.magustek.szjh.plan.bean.PlanHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * @author hexin
 */
public interface PlanHeaderDAO extends CrudRepository<PlanHeader, Long> {
    Page<PlanHeader> findAllByBukrsAndOrgvalAndRptypOrderByIdDesc(String bukrs, String orgval, String rptyp, Pageable pageable);

    PlanHeader findTopByRptypOrderByCkdateDesc(String rptyp);
    PlanHeader findTopByCkdateStartsWithOrderByCkdateDesc(String ckdate);


    Page<PlanHeader> findAllByBukrsAndOrgvalAndRptypAndStonrAndBstaInOrderByIdDesc(String bukrs, String orgval, String rptyp, String stonr, List<String> list, Pageable pageable);
    PlanHeader findById(Long id);
    List<PlanHeader> findAllByJhvalContains(String jhval);
}
