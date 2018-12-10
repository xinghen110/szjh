package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：业务计算指标
 * */
@Getter
@Setter
@Entity
public class IEPlanCalculationSet extends BaseEntity {
    @Column(nullable = false, length = 4) private String caart;//业务计算指标
    @Column(length = 50) private String canam;//业务计算指标描述
    @Column(length = 10) private String vtype;//结果类型
    @Column private String calcu;//计算公式
    @Column(length = 2)  private String zsort;//排序
    @Column(length = 1)  private String msgtype;//消息类型
    @Column(length = 220)private String msgtext;//消息文本
}
