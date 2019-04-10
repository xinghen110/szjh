package com.magustek.szjh.approval.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 审批日志表
 * */
@Getter
@Setter
@Entity
public class ApprovalLog extends BaseEntity {
    @Column(length = 20) private Long headerid;   //抬头表id
    @Column(length = 4) private String bukrs;       //公司代码
    @Column(length = 25) private String butxt;   //公司名称
    @Column(length = 1) private String hjbgn;       //第一个环节:X
    @Column(length = 1) private String hjend;       //最后一个环节:X
    @Column(length = 4) private String bgnstat;     //初始状态
    @Column(length = 4) private String ebsta;      //流转状态
    @Column(length = 40) private String hjtxt;  //环节描述
    @Column(length = 12) private String spnam;      //审批人
    @Column(length = 4) private String ebtyp;       //流转性质(即审批意见AP/RJ)
    @Column(length = 220) private String apinfo;       //审批说明
    @Column(length = 12) private String sbname;      //审批计划提交人
}
