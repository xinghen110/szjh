package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：屏幕行项目配置
 * */
@Getter
@Setter
@Entity
public class IEPlanScreenItemSet extends BaseEntity{
    @Column(length = 4) private String hdnum;       //抬头编号
    @Column(length = 10) private String imnum;      //项目编号
    @Column(length = 50) private String imtxt;      //项目编号
    @Column(length = 30) private String fdnam;      //字段名
    @Column(length = 60) private String fdtxt;      //字段名称
    @Column(length = 50) private String sdvar;      //取数变量名
    @Column(length = 50) private String suvar;      //汇总变量名
    @Column(length = 10) private String color;      //字体颜色
    @Column              private String colcd;      //字体颜色条件
    @Column(length = 10) private String posit;      //字段位置
    @Column(length = 10) private String width;      //宽度
    @Column(length = 1) private String newln;       //换行标识
    @Column(length = 1) private String hyplk;       //超链接标识
    @Column(length = 1) private String freez;       //冻结标识
    @Column(length = 1) private String hiden;       //隐藏标识
    @Column(length = 10) private String vtype;      //取值类型
    @Column(length = 10) private String frtyp;      //冻结类型
    @Column              private int zsort;         //排序
    @Column(length = 1) private String msgtype;     //消息类型
    @Column(length = 220) private String msgtext;   //消息文本

}
