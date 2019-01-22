package com.magustek.szjh.plan.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 计划表：计划明细表
 * */
@Getter
@Setter
@Entity
public class PlanItem extends BaseEntity {
    public static transient final String ZTIME_Y = "Y";//时间维度-年
    public static transient final String ZTIME_M = "M";//时间维度-月
    @Column(nullable = false) private Long headerId;   //计划抬头表ID
    @Column(length = 4)  private String zbart;   //经营指标
    @Column(length = 20) private String zbval;   //经营指标值
    @Column(length = 4)  private String dmart;   //维度指标
    @Column(length = 20) private String dmval;   //维度指标值
    @Column(length = 4)  private String ztime;   //时间维度（y-年，m-月，d-日，s-季度）
    @Column(length = 20) private String ztval;   //时间维度值
    @Column(length = 1)  private String opera;   //业务状态（S取数/C计算/M手工，未启用）
}
