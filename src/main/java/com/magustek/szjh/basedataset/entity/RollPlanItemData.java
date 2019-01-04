package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 计划业务表：滚动计划明细-日期项目
 *
 * */
@Getter
@Setter
@Entity
public class RollPlanItemData extends BaseEntity {
    @ManyToOne(cascade={CascadeType.REFRESH},fetch=FetchType.EAGER,optional=false)
    @JoinColumn(name="head_id") private RollPlanHeadData headId;//抬头id
    @Column(length = 10)   private String imnum;      //项目编号
    @Column(length = 10)   private String dtval;      //日期
    @Column(length = 1000) private String stval;      //合同条款
    @Column(length = 1)    private String ctdtp;      //值类型（C|G）
    @Column private Integer caval;                    //参考能力值
}
