package com.magustek.szjh.approval.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 审批日志表
 * */
@ApiModel(value = "ApprovalLog-审批日志表")
@Getter
@Setter
@Entity
public class ApprovalLog extends BaseEntity {
    @ApiModelProperty(value = "抬头表id")
    @Column(length = 20) private Long headerId;
    @ApiModelProperty(value = "公司代码")
    @Column(length = 4) private String bukrs;
    @ApiModelProperty(value = "公司名称")
    @Column(length = 25) private String butxt;
    @ApiModelProperty(value = "第一个环节:X")
    @Column(length = 1) private String hjbgn;
    @ApiModelProperty(value = "最后一个环节:X")
    @Column(length = 1) private String hjend;
    @ApiModelProperty(value = "初始状态")
    @Column(length = 4) private String bgnstat;
    @ApiModelProperty(value = "流转状态")
    @Column(length = 4) private String ebsta;
    @ApiModelProperty(value = "环节描述")
    @Column(length = 40) private String hjtxt;
    @ApiModelProperty(value = "审批人")
    @Column(length = 12) private String spnam;
    @ApiModelProperty(value = "流转性质(即审批意见AP/RJ)")
    @Column(length = 4) private String ebtyp;
    @ApiModelProperty(value = "审批说明")
    @Column(length = 220) private String apinfo;
    @ApiModelProperty(value = "审批计划提交人")
    @Column(length = 12) private String sbname;
}
