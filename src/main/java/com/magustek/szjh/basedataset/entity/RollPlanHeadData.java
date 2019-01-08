package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 计划业务表：滚动计划明细条目
 *
 * */
@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "version")})
public class RollPlanHeadData extends BaseEntity{
    @Column(nullable = false, length = 14) private String htsno;//合同流水号
    @Column( length = 20) private String htnum;//合同管理编号
    @Column(nullable = false, length = 10) private String hdnum;//抬头编号
    @Column private BigDecimal wears;//金额
    @Column(length = 30) private String version;//明细版本（储存编制日期或计划编号）
    //transient private List<RollPlanItemData> dateItem;  //日期列表
}
