package com.magustek.szjh.Holiday.dao;

import com.magustek.szjh.Holiday.bean.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


public interface HolidayDAO extends CrudRepository<Holiday, Long> {
    Page<Holiday> findAllByYearOrderByYyyymmddAsc(Integer year, Pageable pageable);
    Long countByYear(Integer year);
    Page<Holiday> findAllByYyyymmddGreaterThanAndTypeOrderByYyyymmddAsc(String yyyymmdd, String type, Pageable pageable);
    Page<Holiday> findAllByYyyymmddLessThanAndTypeOrderByYyyymmddDesc(String yyyymmdd, String type, Pageable pageable);
    Holiday findTopByYyyymmdd(String yyyymmdd);
}
