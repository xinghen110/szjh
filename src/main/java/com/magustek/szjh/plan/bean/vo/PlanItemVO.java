package com.magustek.szjh.plan.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "PlanItemVO-计划明细")
public class PlanItemVO {
    @ApiModelProperty(value = "抬头id")
    private Long headerId;
    @ApiModelProperty(value = "z轴")
    private String zaxis;
    @ApiModelProperty(value = "z轴值")
    private String zvalue;
    @ApiModelProperty(value = "行项目值")
    private String[][] value;
    @ApiModelProperty(value = "行项目值")
    private Long[][] itemId;
}
