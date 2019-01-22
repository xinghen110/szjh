package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 计划业务表：滚动计划明细条目
 *
 * */
@Getter
@Setter
@Entity
//TABLE_PER_CLASS策略:父类和子类对应不同的表,子类中存在所有的属性(包含从父类继承下来的所有属性)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(indexes = {@Index(columnList = "version")})
public class RollPlanHeadData extends BaseEntity{
    @Column(length = 4) private String bukrs;                   //公司代码
    @Column(nullable = false, length = 14) private String htsno;//合同流水号
    @Column(length = 20) private String htnum;                  //合同管理编号
    @Column(nullable = false, length = 10) private String hdnum;//抬头编号
    @Column(length = 4) private String zbart;                   //经营指标分类
    @Column private BigDecimal wears = new BigDecimal(0);   //金额
    @Column(length = 10)   private String dtval;                //第一个计划日期(yyyyMMdd)
    @Column(length = 1000) private String stval;                //合同条款
    @Column(length = 30) private String version;                //明细版本（储存编制日期或计划编号）
    //transient private List<RollPlanItemData> dateItem;  //日期列表
}
