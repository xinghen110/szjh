package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：滚动计划-抬头配置
 * */
@Getter
@Setter
@Entity
public class IEPlanBusinessHeadSet extends BaseEntity{

    @Column(nullable = false, length = 4) private String hdnum;//编号
    @Column(length = 4) private String bukrs;//公司代码
    @Column(length = 4) private String rptyp;//报表类型
    @Column(length = 2) private String busta;//业务状态（01-未支付、02-已支付）
    @Column(length = 4) private String ztype;//款项（01-预付款、02-进度款）
    @Column private String condi;//显示条件
    @Column private String variv;//变量
    @Column(length = 4)  private String aflag;//动作标识
    @Column(length = 1)  private String msgtype;//消息类型
    @Column(length = 220)private String msgtext;//消息文本

}
