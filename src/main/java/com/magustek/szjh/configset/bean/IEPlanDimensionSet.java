package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：维度指标
 * */
@Getter
@Setter
@Entity
public class IEPlanDimensionSet extends BaseEntity{


    public static transient final String DM_Company    = "D100";
    public static transient final String DM_Department = "D110";
    public static transient final String DM_User       = "D120";

    @Column(length = 4)  private String dmart;//维度指标
    @Column(length = 50) private String dmnam;//维度指标描述
    @Column(length = 1)  private String dmtyp;//维度类型
    @Column(length = 30) private String fmnam;//取值函数名
    @Column(length = 2)  private String zsort;//指标排序
    @Column(length = 1)  private String swith;//指标开关
    @Column(length = 1)  private String msgtype;//消息类型
    @Column(length = 220)private String msgtext;//消息文本
}
