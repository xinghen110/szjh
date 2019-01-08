package com.magustek.szjh.Holiday.service;

import com.magustek.szjh.Holiday.bean.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface HolidayService {
    Holiday save(Holiday holiday);
    Page<Holiday> listByYear(Integer year, Pageable pageable);
    Iterable<Holiday> addYear(Integer year);
    Holiday getById(Holiday holiday);
    LocalDate getWorkDay(LocalDate from, Integer days, boolean forward) throws Exception;
    LocalDate skipHoliday(LocalDate day, boolean future);
    long getNatureDays(LocalDate from, Integer workDays) throws Exception;
}
