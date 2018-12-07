package com.magustek.szjh.configset.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "PlanHeader-计划配置明细")
@Data
public class IEPlanReportItemVO {
    @ApiModelProperty(value = "公司代码")
    private String bukrs;
    @ApiModelProperty(value = "报表类型")
    private String rptyp;
    @ApiModelProperty(value = "经营指标分类")
    private String zbart;
    @ApiModelProperty(value = "经营指标分类描述")
    private String zbnam;
    @ApiModelProperty(value = "排序")
    private String zsort;
    @ApiModelProperty(value = "经营指标收支类型")
    private String ietyp;
    @ApiModelProperty(value = "操作方式")
    private String opera;
    @ApiModelProperty(value = "计算公式")
    private String calcu;
}
