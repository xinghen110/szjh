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
    @Column(nullable = false, unique = true, length = 4) private String buspt;//业务取数指标
    @Column(length = 4) private String zbart;//经营指标分类
    @Column(length = 30)private String tbnam;//表名
    @Column(length = 30)private String fdnam;//字段名
    @Column(length = 10)private String vtype;//取值类型
    @Column private String condi;//条件
    @Column(length = 1)private String msgtype;//消息类型
    @Column(length = 220)private String msgtext;//消息文本

}
