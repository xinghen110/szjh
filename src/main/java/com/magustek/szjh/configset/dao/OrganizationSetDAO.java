package com.magustek.szjh.configset.dao;

import com.magustek.szjh.configset.bean.OrganizationSet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrganizationSetDAO extends CrudRepository<OrganizationSet,Long> {
    OrganizationSet findTopByBukrsOrderByCsortAsc(String bukrs);

    @Query(value = "select distinct dpnum,dpnam,csort,dsort from OrganizationSet where bukrs = ?1 order by csort,dsort asc ")
    List<Object[]> findDistinctDpnumByBukrs(String bukrs);
    @Query(value = "select distinct ponum,ponam,csort,dsort from OrganizationSet where bukrs = ?1 order by csort,dsort asc ")
    List<Object[]> findDistinctPonumByBukrs(String bukrs);
    @Query(value = "select distinct uname,usnam,csort,dsort from OrganizationSet where bukrs = ?1 order by csort,dsort asc ")
    List<Object[]> findDistinctUnameByBukrs(String bukrs);

    @Query(value = "select distinct ponum,ponam,csort,dsort from OrganizationSet where dpnum = ?1 order by csort,dsort asc ")
    List<Object[]> findDistinctPonumByDpnum(String dpnum);
    @Query(value = "select distinct uname,usnam,csort,dsort from OrganizationSet where dpnum = ?1 order by csort,dsort asc ")
    List<Object[]> findDistinctUnameByDpnum(String dpnum);


    @Query(value = "select distinct bukrs, hunit, hvalu, csort from OrganizationSet order by csort asc ")
    List<Object[]> findDistinctBukrs();

    List<OrganizationSet> findDistinctBukrsByOrderByCsort();
    List<OrganizationSet> findDistinctDpnumByOrderByDsort();
    List<OrganizationSet> findDistinctPonumByOrderByDsort();
    List<OrganizationSet> findDistinctUnameByOrderByDsort();
}
