package com.magustek.szjh.Holiday.service.impl;

import com.magustek.szjh.Holiday.bean.Holiday;
import com.magustek.szjh.Holiday.dao.HolidayDAO;
import com.magustek.szjh.Holiday.service.HolidayService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component("HolidayService")
public class HolidayServiceImpl implements HolidayService {
    private HolidayDAO holidayDao;

    public HolidayServiceImpl(HolidayDAO holidayDao) {
        this.holidayDao = holidayDao;
    }

    //修改日期属性，其他不允许修改
    @Override
    public Holiday save(Holiday holiday) {
        Assert.notNull(holiday.getId(), "ID不能为空！");
        Holiday old = holidayDao.findOne(holiday.getId());
        old.setType(holiday.getType());
        if(Holiday.work_day.equals(holiday.getType())){
            old.setTypeDesc(Holiday.work_day_txt);
        }else{
            old.setTypeDesc(Holiday.holiday_day_txt);
        }

        return holidayDao.save(old);
    }

    @Override
    public Page<Holiday> listByYear(Integer year, Pageable pageable) {
        Assert.notNull(year, "年份不能为空！");
        return holidayDao.findAllByYearOrderByYyyymmddAsc(year, pageable);
    }

    @Override
    public Iterable<Holiday> addYear(Integer year) {
        Long count = holidayDao.countByYear(year);
        if(count!=null){
            Assert.isTrue(count <= 0, year+"年日期数据已存在！");
        }
        //当年的第一天
        LocalDate firstDay = LocalDate.ofYearDay(year, 1);
        List<Holiday> list = new ArrayList<>(firstDay.lengthOfYear());

        for(int i=1;i<=firstDay.lengthOfYear();i++){
            LocalDate date = LocalDate.ofYearDay(year, i);
            Holiday holiday = new Holiday();
            holiday.setYear(year);
            holiday.setMonth(date.getMonthValue());
            holiday.setDay(date.getDayOfMonth());
            holiday.setYyyymmdd(date.toString());
            //工作日、节假日
            DayOfWeek week = date.getDayOfWeek();
            if( week.equals(DayOfWeek.SATURDAY) || week.equals(DayOfWeek.SUNDAY)){
                holiday.setType(Holiday.holiday_day);
                holiday.setTypeDesc(Holiday.holiday_day_txt);
            }else{
                holiday.setType(Holiday.work_day);
                holiday.setTypeDesc(Holiday.work_day_txt);
            }
            holiday.setWeek(week.getDisplayName(TextStyle.SHORT, Locale.SIMPLIFIED_CHINESE));
            list.add(holiday);
        }
        return holidayDao.save(list);
    }

    @Override
    public Holiday getById(Holiday holiday) {
        Assert.notNull(holiday.getId(), "ID不能为空！");
        return holidayDao.findOne(holiday.getId());
    }

    /**
     * 根据传入的基准日期、向前\向后、天数，返回工作日，例如：2018-04-03后第10个工作日是2018-04-17，之前第10个工作日是2018-03-20
     * @param from  基准日期
     * @param days  计算天数
     * @param forward   true未来，false历史
     * @return  工作日
     */
    @Override
    public LocalDate getWordDay(LocalDate from, Integer days, boolean forward) throws Exception{
        PageRequest page = new PageRequest(days-1, 1);
        Page<Holiday> workDay;
        if(forward){
            workDay = holidayDao.findAllByYyyymmddGreaterThanAndTypeOrderByYyyymmddAsc(from.toString(), Holiday.work_day, page);
        }else{
            workDay = holidayDao.findAllByYyyymmddLessThanAndTypeOrderByYyyymmddDesc(from.toString(), Holiday.work_day, page);
        }
        if(workDay.getSize()<1){
            throw new Exception("获取工作日失败！");
        }else{
            return LocalDate.parse(workDay.getContent().get(0).getYyyymmdd());
        }
    }
}
