package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 计划业务表：历史业务取数主表
 * */
@Getter
@Setter
@Entity
public class IEPlanContractHeadSet extends BaseEntity {
    @Column(length = 20, unique = true, nullable = false) private String htnum;
    @Column(length = 14, nullable = false)private String htsno;
    //@Column(length = 8)private String chdate;
    //@Column(length = 6)private String chtime;
}
