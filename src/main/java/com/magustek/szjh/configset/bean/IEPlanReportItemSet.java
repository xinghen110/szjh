package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 配置表：报表项目
 * */
@Getter
@Setter
@Entity
public class IEPlanReportItemSet extends BaseEntity {
    @Column(length = 4) private String bukrs;                  //公司代码
    @Column(length = 4) private String rptyp;                  //报表类型
    @Column(length = 4) private String zbart;                  //经营指标分类
    @Column(length = 11)private String ietyp;                  //经营指标收支类型
    @Column(length = 4) private String sdart;                  //经营指标分类（计算所需取值指标）
    @Column(length = 1) private String opera;                  //操作方式
    @Column private String calcu;                              //计算公式
    private Integer zblev;                                     //层级
    private Integer zsort;                                     //排序
    @Column(length = 1) private String msgtype;                //消息类型
    @Column(length = 220) private String msgtext;              //消息文本

}
