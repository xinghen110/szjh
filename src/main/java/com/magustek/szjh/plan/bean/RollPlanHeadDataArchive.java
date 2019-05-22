package com.magustek.szjh.plan.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * 计划表：合同滚动计划抬头数据（每个计划一版）
 *
 * @author hexin*/
@ApiModel(value = "RollPlanHeadDataArchive-合同滚动计划抬头数据")
@Getter
@Setter
@Entity
public class RollPlanHeadDataArchive  extends BaseEntity {
    @ApiModelProperty(value = "月计划-id")
    @Column(nullable = false)private Long planHeadId;
    @ApiModelProperty(value = "RollPlanHeadData-id")
    @Column(nullable = false)private Long rollId;               //滚动计划抬头ID
    @ApiModelProperty(value = "合同的维度数据，格式-D100:6010,D110:50003521,D120:SHIHAO1,")
    @Column private String dmval;

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
    @ApiModelProperty(value = "明细版本（储存编制日期或计划编号）")
    @Column(length = 30) private String version;                //明细版本（储存编制日期或计划编号）

}
