package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
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
    @Column(length = 8) private String wflsh;       //工作流审批组流水号
    @Column(length = 4) private String sphjh;       //审批环节代码
    @Column(length = 4) private String bukrs;       //公司代码
    @Column(length = 1) private String hjbgn;       //第一个环节:X
    @Column(length = 1) private String hjend;       //最后一个环节:X
    @Column(length = 4) private String bbsta;       //审批状态
    @Column(length = 4) private String ebtyp;       //流转性质
    @Column(length = 30) private String ebsta;      //流转状态
    @Column(length = 40)private  String hjtxt;     //环节描述
    @Column(length = 12) private String spnam;      //审批人
    @Column(length = 1) private String reflg;       //状态:已下达
    @Column(length = 1) private String msgtype;     //消息类型
    @Column(length = 220) private String msgtext;   //消息文本
    @Column(length = 4) private String aflag;       //动作标识
}
