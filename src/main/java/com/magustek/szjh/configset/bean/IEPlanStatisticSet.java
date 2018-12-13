package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：统计指标配置
 * */
@Getter
@Setter
@Entity
public class IEPlanStatisticSet extends BaseEntity{
    @Column(length = 4)  private String bukrs;//公司代码
    @Column(length = 4)  private String rptyp;//报表类型
    @Column(length = 4)  private String tmart;//时间轴指标
    @Column(length = 40) private String tmnam;//时间轴指标描述
    @Column private String calcu;             //计算公式
    @Column private int zsort;                //排序
    @Column(length = 4)  private String aflag;//动作标识
    @Column(length = 1)  private String msgtype;//消息类型
    @Column(length = 220)private String msgtext;//消息文本
}
