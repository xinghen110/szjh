package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：屏幕抬头配置
 * */
@Getter
@Setter
@Entity
public class IEPlanScreenHeadSet extends BaseEntity {
    @Column(length = 4) private String hdnum;//抬头编号
    @Column(length = 50) private String hdtxt;//抬头编号描述
    @Column(length = 4) private String bukrs;//公司代码
    @Column(length = 4) private String rptyp;//报表类型
    @Column private String hview;//报表编号
    @Column private String intfa;//接口
    @Column(length = 1) private String msgtype;//消息类型
    @Column(length = 220) private String msgtext;//消息文本
}
