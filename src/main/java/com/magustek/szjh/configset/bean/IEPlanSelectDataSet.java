package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：业务取数指标
 * */
@Getter
@Setter
@Entity
public class IEPlanSelectDataSet extends BaseEntity{
    @Column(length = 4)  private String sdart;//业务取数指标
    @Column(length = 50) private String sdnam;//业务取数指标描述
    @Column(length = 4)  private String zbart;//经营指标分类（一期暂不启用）
    @Column(length = 4)  private String psdar;//前业务取数指标
    @Column(length = 4)  private String nsdar;//后业务取数指标
    @Column(length = 10) private String vtype;//取值类型
    @Column(length = 30) private String fmnam;//功能模块的名称
    @Column(length = 1)  private String msgtype;//消息类型
    @Column(length = 220)private String msgtext;//消息文本
}
