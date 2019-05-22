package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 计划业务表：滚动计划明细条目
 *
 * */
@ApiModel(value = "滚动计划抬头")
@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "version")})
public class RollPlanHeadData extends BaseEntity{
    @ApiModelProperty(value = "公司代码")
    @Column(length = 4) private String bukrs;                   //公司代码
    @ApiModelProperty(value = "合同流水号")
    @Column(nullable = false, length = 14) private String htsno;//合同流水号
    @ApiModelProperty(value = "合同管理编号")
    @Column(length = 20) private String htnum;                  //合同管理编号
    @ApiModelProperty(value = "抬头编号")
    @Column(nullable = false, length = 10) private String hdnum;//抬头编号
    @ApiModelProperty(value = "经营指标分类")
    @Column(length = 4) private String zbart;                   //经营指标分类
    @ApiModelProperty(value = "金额")
    @Column private BigDecimal wears = new BigDecimal(0);   //金额
    @ApiModelProperty(value = "第一个计划日期(yyyyMMdd)")
    @Column(length = 10)   private String dtval;                //第一个计划日期(yyyyMMdd)
    @ApiModelProperty(value = "合同条款")
    @Column(length = 1000) private String stval;                //合同条款
    @ApiModelProperty(value = "版本")
    @Column(length = 30) private String version;                //明细版本（储存编制日期或计划编号）
    //transient private List<RollPlanItemData> dateItem;  //日期列表
}
