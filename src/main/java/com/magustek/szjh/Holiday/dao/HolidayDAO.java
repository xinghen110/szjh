package com.magustek.szjh.Holiday.dao;

import com.magustek.szjh.Holiday.bean.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface HolidayDAO extends CrudRepository<Holiday, Long> {
    Page<Holiday> findAllByYearOrderByMonthAscDayAsc(Integer year, Pageable pageable);
    Long countByYear(Integer year);
}
