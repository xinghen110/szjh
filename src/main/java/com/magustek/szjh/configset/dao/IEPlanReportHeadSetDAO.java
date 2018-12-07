package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.IEPlanReportHeadSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanReportHeadSetDAO extends CrudRepository<IEPlanReportHeadSet, Long> {
    IEPlanReportHeadSet findByBukrsAndRptyp(String bukrs, String rptyp);
    List<IEPlanReportHeadSet> findAllByRptyp(String rptyp);
}
