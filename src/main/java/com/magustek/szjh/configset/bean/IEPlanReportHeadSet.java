package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：报表抬头
 * */
@Getter
@Setter
@Entity
public class IEPlanReportHeadSet extends BaseEntity {
    @Column(nullable = false, length = 4) private String bukrs; //公司代码
    @Column(nullable = false, length = 4) private String rptyp; //报表类型
    @Column(length = 4) private String xaxis;                   //X轴
    @Column(length = 4) private String yaxis;                   //Y轴
    @Column(length = 4) private String zaxis;                   //z轴
    @Column(length = 1) private String zbdep;                   //指标深度
    @Column(length = 1) private String orgdp;                   //组织深度
    //@Column(length = 1) private String hunit;                   //历史维度单位
    //@Column(length = 5) private String hvalu;                   //历史维度值
    @Column(length = 1) private String punit;                   //计划维度单位
    @Column(length = 5) private String pvalu;                   //计划维度值
    @Column(length = 1) private String tflag;                   //绝对时间标识
    @Column(length = 1) private String msgtype;                 //消息类型
    @Column(length = 220) private String msgtext;               //消息文本
}
