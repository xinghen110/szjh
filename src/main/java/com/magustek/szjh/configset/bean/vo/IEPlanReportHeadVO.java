package com.magustek.szjh.configset.bean.vo;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.utils.KeyValueBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "PlanHeader-计划抬头配置")
@Data
public class IEPlanReportHeadVO {
    @ApiModelProperty(value = "公司代码")
    private String bukrs;
    @ApiModelProperty(value = "报表类型")
    private String rptyp;
    @ApiModelProperty(value = "X轴类型")
    private String xaxis;
    @ApiModelProperty(value = "X轴值列表")
    private ArrayList<KeyValueBean> xvalue;
    @ApiModelProperty(value = "Y轴类型")
    private String yaxis;
    @ApiModelProperty(value = "Y轴值列表")
    private ArrayList<KeyValueBean> yvalue;
    @ApiModelProperty(value = "z轴类型")
    private String zaxis;
    @ApiModelProperty(value = "z轴值列表")
    private ArrayList<KeyValueBean> zvalue;
    @ApiModelProperty(value = "指标深度")
    private String zbdep;
    @ApiModelProperty(value = "组织深度")
    private String dmart;
    @ApiModelProperty(value = "历史维度单位")
    private String hunit;
    @ApiModelProperty(value = "历史维度值")
    private String hvalu;
    @ApiModelProperty(value = "计划维度单位")
    private String punit;
    @ApiModelProperty(value = "计划维度值")
    private String pvalu;
    @ApiModelProperty(value = "报表日期（年报传yyyy，月报传yyyy-MM）")
    private String rpdat;
    @ApiModelProperty(value = "绝对时间标识（X为绝对时间）")
    private String tflag;
    @ApiModelProperty(value = "行项目VO")
    private List<IEPlanReportItemVO> itemVOS;

    public String toJson(){
        return JSON.toJSONString(this);
    }
}
