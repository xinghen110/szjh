package com.magustek.szjh.plan.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * 计划表：计划布局表
 * */
@ApiModel(value = "PlanLayout-计划布局")
@Getter
@Setter
@Entity
public class PlanLayout extends BaseEntity{
    @ApiModelProperty(value = "计划id（headerId）")
    @Column(nullable = false, unique = true) private Long headerId;
    @ApiModelProperty(value = "布局json")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(columnDefinition = "text") private String layout;
}
