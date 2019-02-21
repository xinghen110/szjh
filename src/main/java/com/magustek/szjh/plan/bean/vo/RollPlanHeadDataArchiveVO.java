package com.magustek.szjh.plan.bean.vo;

import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * 计划表：合同滚动计划抬头数据（每个计划一版）
 * */
@ApiModel(value = "计划相关-合同滚动计划抬头数据")
@Getter
@Setter
public class RollPlanHeadDataArchiveVO extends RollPlanHeadDataArchive {

    private String caart;   //业务计算指标
    private String dmart;   //维度指标
    private Integer caval;   //历史能力值
}
