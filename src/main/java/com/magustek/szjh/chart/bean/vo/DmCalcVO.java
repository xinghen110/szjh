package com.magustek.szjh.chart.bean.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class DmCalcVO {
    @NotNull
    private String startDate;   //起始日期
    @NotNull
    private String endDate;     //截止日期
    @Size(min = 1)
    private String[] dmval;     //组织机构代码列表
    @NotNull
    private String caart;       //业务计算指标

}
