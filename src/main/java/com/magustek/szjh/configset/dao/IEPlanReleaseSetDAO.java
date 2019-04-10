package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.IEPlanReleaseSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEPlanReleaseSetDAO extends CrudRepository<IEPlanReleaseSet, Long> {
    IEPlanReleaseSet findByBukrsAndBbsta(String bukrs, String bbsta);
    IEPlanReleaseSet findByBukrsAndHjbgn(String bukrs, String hjbgn);
    IEPlanReleaseSet findByBukrsAndHjend(String bukrs, String hjend);
    List<IEPlanReleaseSet> findAllBySpnamAndBukrsAndWflsh(String userName,String bukrs,String wflsh);
}
