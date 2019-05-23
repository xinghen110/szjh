package com.magustek.szjh.plan.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 计划表：合同滚动计划抬头数据（每个计划一版）
 * */
@ApiModel(value = "RollPlanItemDataArchive-合同滚动计划抬头数据")
@Getter
@Setter
@Entity
public class RollPlanItemDataArchive extends BaseEntity {
    //@Column(nullable = false)private Long headArchiveId;//归档的滚动计划抬头ID
    @ApiModelProperty(value = "月计划ID")
    @Column(nullable = false)private Long planHeadId;//计划抬头ID（使用父类的-headId与headArchive关联）
    @ApiModelProperty(value = "滚动计划抬头id")
    @Column private Long headId;                      //抬头id
    @ApiModelProperty(value = "项目编号")
    @Column(length = 10)   private String imnum;      //项目编号
    @ApiModelProperty(value = "计划日期")
    @Column(length = 10)   private String dtval;      //日期
    @ApiModelProperty(value = "合同条款")
    @Column(length = 1000) private String stval;      //合同条款
    @ApiModelProperty(value = "值类型（C|G）")
    @Column(length = 1)    private String ctdtp;      //值类型（C|G）
    @ApiModelProperty(value = "业务取数指标")
    @Column(length = 4)    private String sdart;      //业务取数指标
    @ApiModelProperty(value = "是否超过合同约定时间")
    @Column(length = 1)    private String odue;       //是否超过合同约定时间（O）
    @ApiModelProperty(value = "参考能力值")
    @Column private Integer caval;                    //参考能力值
}
