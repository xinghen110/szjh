package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 计划业务表：历史业务维度明细表
 * */
@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "version")})
public class IEPlanDimenValueSet extends BaseEntity{
    @Column(length = 14) private String htsno;//合同流水号
    @Column(length = 20) private String htnum;//合同管理编号
    @Column(length = 50) private String begda;//开始日期
    @Column(length = 50) private String endda;//结束日期
    @Column(length = 4)  private String dmart;//维度指标
    @Column              private String dmval;//维度指标值
    @Column(length = 1)  private String hflag;//历史计算标识
    @Column(length = 1)  private String pflag;//计划编制标识
    @Column(length = 30) private String version;//明细版本（储存编制日期或计划编号）
}
