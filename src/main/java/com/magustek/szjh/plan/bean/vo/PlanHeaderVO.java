package com.magustek.szjh.plan.bean.vo;

import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.utils.KeyValueBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
    @ApiModelProperty(value = "审批意见")
    private String apinfo;
    @ApiModelProperty(value = "下一步审批人环节描述")
    private String nthjtxt;
    @ApiModelProperty(value = "下一个审批人部门")
    private String ntdpnam;
    @ApiModelProperty(value = "下一个审批人岗位名称")
    private String ntponum;
    @ApiModelProperty(value = "下一个审批人拼音名字")
    private String ntuname;
    @ApiModelProperty(value = "下一个审批人中文名字")
    private String ntusnam;
    @ApiModelProperty(value = "当前审批人岗位")
    private String curponum;
    @ApiModelProperty(value = "前端模块描述 1:计划编制；2：计划查询；3：计划审批")
    private String powerModel;
    @ApiModelProperty(value = "审批模式 1：提交；2：同意；3：驳回")
    private String approvalMode;

    private String zbart;
    private String dtval;
    private String dmval;






}
