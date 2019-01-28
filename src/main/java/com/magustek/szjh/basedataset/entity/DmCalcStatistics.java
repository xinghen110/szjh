package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 计划业务表：历史业务维度统计
 * */
@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "version")})
public class DmCalcStatistics extends BaseEntity{
    @Column(nullable = false, length = 4) private String dmart;//维度指标
    @Column                               private String dmval;//维度指标值

    @Column(nullable = false, length = 4) private String caart;//业务计算指标
    @Column                               private String caval;//业务计算指标值

    @Column                               private Integer jswdqz;//业务计算指标-维度权重值
    @Column                               private Integer hisval;//业务计算指标-历史能力值
    @Column(length = 30)                  private String version;//明细版本（储存编制日期或计划编号）
}
