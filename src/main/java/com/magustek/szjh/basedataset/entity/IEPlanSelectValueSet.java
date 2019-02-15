package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 计划业务表：历史业务取数明细表
 *
 * 注意：如果指标返回类型是日期，odata返回格式为【yyyyMMdd】。
 *
 * */
@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "version"),
                  @Index(columnList = "htsno,version")})
public class IEPlanSelectValueSet extends BaseEntity {
    @Column(nullable = false, length = 14) private String htsno;//合同流水号
    @Column(nullable = false, length = 20) private String htnum;//合同管理编号
    @Column(length = 50) private String begda;//开始日期
    @Column(length = 50) private String endda;//结束日期
    @Column(nullable = false, length = 4) private String sdart;//业务取数指标
    @Column(nullable = false) private String sdval;//业务取数指标值
    @Column(length = 1) private String hflag;//历史计算标识
    @Column(length = 1) private String pflag;//计划编制标识
    @Column(length = 4) private String bukrs;//公司代码
    @Column(length = 30) private String version;//明细版本（储存编制日期或计划编号）
    @Column(length = 1) private String referenced;//是否已被引用（被计划引用的数据不允许删除）
}
