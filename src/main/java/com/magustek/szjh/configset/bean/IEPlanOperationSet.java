package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
/**
 * 配置表：经营指标分类
 * */
@Getter
@Setter
@Entity
public class IEPlanOperationSet extends BaseEntity{
    @Column(nullable = false, length = 4) private String zbart; //经营指标分类
    @Column(length = 50) private String zbnam;                  //经营指标分类描述
    @Column(length = 1) private String zbflg;                   //经营指标资金流向
    @Column(length = 2) private String zblev;                   //层级
    @Column(length = 2) private String zsort;                   //排序
    @Column(length = 1) private String swith;                   //开关
    @Column(length = 1) private String msgtype;                 //消息类型
    @Column(length = 220) private String msgtext;               //消息文本
}
