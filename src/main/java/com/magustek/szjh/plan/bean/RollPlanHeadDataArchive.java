package com.magustek.szjh.plan.bean;

import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 计划表：合同滚动计划抬头数据（每个计划一版）
 * */
@ApiModel(value = "计划相关-合同滚动计划抬头数据")
@Getter
@Setter
@Entity
public class RollPlanHeadDataArchive extends RollPlanHeadData {
    @Column(nullable = false)private Long planHeadId;
    @Column private String dmval;//合同的维度数据，格式-D100:6010,D110:50003521,D120:SHIHAO1,
}
