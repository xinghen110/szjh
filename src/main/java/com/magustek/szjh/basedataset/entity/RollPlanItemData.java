package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 计划业务表：滚动计划明细-日期项目
 *
 * */
@ApiModel(value = "滚动计划行项目")
@Getter
@Setter
@Entity
public class RollPlanItemData extends BaseEntity {
    @ApiModelProperty(value = "RollPlanHeadData-id")
    @ManyToOne(cascade={CascadeType.REFRESH},fetch=FetchType.EAGER,optional=false)
    @JoinColumn(name="head_id") private RollPlanHeadData headId;//抬头id
    @ApiModelProperty(value = "项目编号")
    @Column(length = 10)   private String imnum;      //项目编号
    @ApiModelProperty(value = "日期")
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
