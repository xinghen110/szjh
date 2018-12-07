package com.magustek.szjh.plan.bean.vo;

import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.utils.KeyValueBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;

@Getter
@Setter
@ApiModel(value = "PlanHeaderVO-计划抬头")
public class PlanHeaderVO extends PlanHeader{
    @ApiModelProperty(value = "计划的指标id，以及指标值")
    private ArrayList<KeyValueBean> zbList;
    @ApiModelProperty(value = "公司名称")
    private String butxt;
    @ApiModelProperty(value = "工作状态描述")
    private String sptxt;
    @ApiModelProperty(value = "业务状态描述")
    private String bstxt;
    @ApiModelProperty(value = "报表类型描述")
    private String rptxt;
    @ApiModelProperty(value = "货币描述")
    private String ktext;
    @ApiModelProperty(value = "货币单位描述")
    private String unitx;
}
