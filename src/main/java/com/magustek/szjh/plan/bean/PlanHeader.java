package com.magustek.szjh.plan.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 计划表：计划抬头表
 * */
@ApiModel(value = "PlanHeader-计划抬头")
@Getter
@Setter
@Entity
public class PlanHeader extends BaseEntity{
    @ApiModelProperty(value = "公司代码-后台获取")
    @Column(nullable = false, length = 4) private String bukrs;
    @ApiModelProperty(value = "报表级别（C-公司报表|D-部门报表|P-岗位报表|U-用户报表）")
    @Column(length = 4)   private String rporg;
    @ApiModelProperty(value = "报表级别值-后台获取（公司代码|部门代码|岗位代码|用户代码）")
    @Column(length = 20)  private String orgval;
    @ApiModelProperty(value = "工作状态")
    @Column(length = 2)   private String stonr;
    @ApiModelProperty(value = "业务状态")
    @Column(length = 3)   private String bsta;
    @ApiModelProperty(value = "报表类型")
    @Column(length = 4)   private String rptyp;
    @ApiModelProperty(value = "计划类型对应值（时间）")
    @Column(length = 12)  private String jhval;
    @ApiModelProperty(value = "计划描述")
    @Column(length = 120) private String jhtxt;
    @ApiModelProperty(value = "新计划标记")
    @Column(length = 1)   private String nflag;
    @ApiModelProperty(value = "流程版本号")
    @Column(length = 4)   private String modve;
    @ApiModelProperty(value = "货币代码")
    @Column(length = 5)   private String waers;
    @ApiModelProperty(value = "单位（元、万元、亿元）")
    @Column(length = 10)  private String unit;
    @ApiModelProperty(value = "编制参考日期（yyyy-MM-dd）")
    @Column(length = 10)  private String ckdate;
}
