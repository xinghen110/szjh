package com.magustek.szjh.Holiday.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 合同管理:节假日配置功能
 * */
@Getter
@Setter
@Entity
public class Holiday extends BaseEntity {
    @Column(nullable = false) private Integer year;                     //年
    @Column(nullable = false) private Integer month;                    //月
    @Column(nullable = false) private Integer day;                      //日
    @Column(nullable = false, length = 10) private String yyyymmdd;     //日期字符串（格式yyyy-mm-dd）
    @Column(length = 4, nullable = false) private String week;          //星期

    @Column(length = 4, nullable = false) private String type;          //日期类型
    public transient static String work_day = "W";                      //工作日
    public transient static String work_day_txt = "工作日";             //工作日
    public transient static String holiday_day = "H";                   //节假日
    public transient static String holiday_day_txt = "节假日";          //节假日
    @Column(length = 40, nullable = false) private String typeDesc;     //日期类型描述

    @Column(length = 40) private String mark;                           //备注-预留字段
}
