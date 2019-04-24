package com.magustek.szjh.configset.bean;

import com.magustek.szjh.plan.bean.vo.PlanHeaderVO;
import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：审批流程配置
 * */
@Getter
@Setter
@Entity
public class IEPlanReleaseSet extends BaseEntity {
    @ApiModelProperty(value = "工作流审批组流水号")
    @Column(length = 8) private String wflsh;
    @ApiModelProperty(value = "审批环节代码")
    @Column(length = 4) private String sphjh;
    @ApiModelProperty(value = "公司代码")
    @Column(length = 4) private String bukrs;
    @ApiModelProperty(value = "第一个环节:X")
    @Column(length = 1) private String hjbgn;
    @ApiModelProperty(value = "最后一个环节:X")
    @Column(length = 1) private String hjend;
    @ApiModelProperty(value = "审批状态")
    @Column(length = 4) private String bbsta;
    @ApiModelProperty(value = "流转性质")
    @Column(length = 4) private String ebtyp;
    @ApiModelProperty(value = "流转状态")
    @Column(length = 30) private String ebsta;
    @ApiModelProperty(value = "环节描述")
    @Column(length = 40)private  String hjtxt;
    @ApiModelProperty(value = "审批人")
    @Column(length = 12) private String spnam;
    @ApiModelProperty(value = "状态:已下达")
    @Column(length = 1) private String reflg;
    @ApiModelProperty(value = "消息类型")
    @Column(length = 1) private String msgtype;
    @ApiModelProperty(value = "消息文本")
    @Column(length = 220) private String msgtext;
    @ApiModelProperty(value = "动作标识")
    @Column(length = 4) private String aflag;

    public void dealWithHjtxt(IEPlanReleaseSet ie, PlanHeaderVO vo){
        if(ie != null){
            String str = ie.getHjtxt();
            String[] strs = str.split(",");
            vo.setNtdpnam(strs[0]);
            vo.setNtponum(strs[1]);
            vo.setNtusnam(strs[2]);
            vo.setNtuname(ie.getSpnam());
        }else{
            vo.setNtdpnam("");
            vo.setNtponum("");
            vo.setNtusnam("");
            vo.setNtuname("");
        }
    }
}
