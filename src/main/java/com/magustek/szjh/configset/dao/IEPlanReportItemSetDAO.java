package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.IEPlanReportItemSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanReportItemSetDAO extends CrudRepository<IEPlanReportItemSet, Long> {
    List<IEPlanReportItemSet> findAllByBukrsAndRptypOrderByZsort(String bukrs, String rptyp);
    List<IEPlanReportItemSet> findAllByIetypIsNotNull();
}
