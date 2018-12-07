package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：组织机构
 * */
@Getter
@Setter
@Entity
public class OrganizationSet extends BaseEntity {
    @Column(length = 4)  private String bukrs;   //公司代码
    @Column(length = 25) private String butxt;   //公司名称
    @Column(length = 10) private String csort;   //公司排序
    @Column(length = 8)  private String dpnum;   //部门编号
    @Column(length = 40) private String dpnam;   //部门名称
    @Column(length = 10) private String dsort;   //部门排序
    @Column(length = 12) private String ponum;   //岗位编号
    @Column(length = 40) private String ponam;   //岗位名称
    @Column(length = 12) private String uname;   //用户名
    @Column(length = 80) private String usnam;   //用户全名
    @Column(length = 1)  private String hunit;   //历史维度单位
    @Column(length = 5)  private String hvalu;   //历史维度值
    @Column(length = 1)  private String msgtype; //消息类型
    @Column(length = 220)private String msgtext; //消息文本

}
