package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.IEPlanScreenHeadSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanScreenHeadSetDAO extends CrudRepository<IEPlanScreenHeadSet, Long> {

    IEPlanScreenHeadSet findTopByBukrsAndRptypAndHview(String bukrs, String rptyp, String hview);

    IEPlanScreenHeadSet findTopByHdnum(String hdnum);
}
