package com.magustek.szjh.plan.bean;

import com.magustek.szjh.basedataset.entity.RollPlanItemData;
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
public class RollPlanItemDataArchive extends RollPlanItemData{
    //@Column(nullable = false)private Long headArchiveId;//归档的滚动计划抬头ID
    @Column(nullable = false)private Long planHeadId;//计划抬头ID（使用父类的-headId与headArchive关联）
}
