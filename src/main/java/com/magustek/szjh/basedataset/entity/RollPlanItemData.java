package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 计划业务表：滚动计划明细-日期项目
 *
 * */
@Getter
@Setter
//TABLE_PER_CLASS策略:父类和子类对应不同的表,子类中存在所有的属性(包含从父类继承下来的所有属性)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Entity
public class RollPlanItemData extends BaseEntity {
    @ManyToOne(cascade={CascadeType.REFRESH},fetch=FetchType.EAGER,optional=false)
    @JoinColumn(name="head_id") private RollPlanHeadData headId;//抬头id
    @Column(length = 10)   private String imnum;      //项目编号
    @Column(length = 10)   private String dtval;      //日期
    @Column(length = 1000) private String stval;      //合同条款
    @Column(length = 1)    private String ctdtp;      //值类型（C|G）
    @Column(length = 4)    private String sdart;      //业务取数指标
    @Column(length = 1)    private String odue;       //是否超过合同约定时间（O）
    @Column private Integer caval;                    //参考能力值
}
