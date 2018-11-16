package com.magustek.szjh.log.service;


import com.google.common.base.Strings;
import com.magustek.szjh.log.dao.LogDao;
import com.magustek.szjh.log.entity.LogEntity;
import com.magustek.szjh.log.entity.LogEntityVO;
import com.magustek.szjh.utils.ClassUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class LogService {
    private final LogDao logDao;

    public LogService(LogDao logDao) {
        this.logDao = logDao;
    }

    public Page<LogEntity> searchLog(LogEntityVO vo){
        //如果起始时间为空则默认往前7天
        if(Strings.isNullOrEmpty(vo.getStart())){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            vo.setStart(ClassUtils.dfFullTime.format(cal.getTime()));
        }
        if(Strings.isNullOrEmpty(vo.getEnd())){
            vo.setEnd(ClassUtils.dfFullTime.format(new Date()));
        }

        if(Strings.isNullOrEmpty(vo.getLevel())){
            vo.setLevel("");
        }

        if(Strings.isNullOrEmpty(vo.getMessage())){
            vo.setMessage("");
        }

        return logDao.findAllByTimeBetweenAndLevelContainingAndMessageContainingOrderByTimeDesc(
                vo.getStart(),
                vo.getEnd(),
                vo.getLevel(),
                vo.getMessage(),
                vo.getPageRequest());
    }

    public LogEntity saveLog(LogEntity log){
        logDao.save(log);
        return log;
    }

    public static void main(String args[]){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        System.out.println(ClassUtils.dfFullTime.format(cal.getTime()));
    }

}
