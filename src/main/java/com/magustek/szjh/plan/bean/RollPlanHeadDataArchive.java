package com.magustek.szjh.plan.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * 计划表：合同滚动计划抬头数据（每个计划一版）
 * */
@ApiModel(value = "计划相关-合同滚动计划抬头数据")
@Getter
@Setter
@Entity
public class RollPlanHeadDataArchive  extends BaseEntity {

    @Column(nullable = false)private Long planHeadId;
    @Column(nullable = false)private Long rollId;               //滚动计划抬头ID
    @Column private String dmval;//合同的维度数据，格式-D100:6010,D110:50003521,D120:SHIHAO1,


    @Column(length = 4) private String bukrs;                   //公司代码
    @Column(nullable = false, length = 14) private String htsno;//合同流水号
    @Column(length = 20) private String htnum;                  //合同管理编号
    @Column(nullable = false, length = 10) private String hdnum;//抬头编号
    @Column(length = 4) private String zbart;                   //经营指标分类
    @Column private BigDecimal wears = new BigDecimal(0);   //金额
    @Column(length = 10)   private String dtval;                //第一个计划日期(yyyyMMdd)
    @Column(length = 1000) private String stval;                //合同条款
    @Column(length = 30) private String version;                //明细版本（储存编制日期或计划编号）

}
